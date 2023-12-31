package com.gotowin.persistance.adapter

import com.gotowin.business.exception.*
import com.gotowin.business.mail.MailService
import com.gotowin.business.mail.SimpleMailSenderRequest
import com.gotowin.business.security.UserContextService
import com.gotowin.core.adapter.UserRepositoryAdapter
import com.gotowin.core.domain.GotowinUser
import com.gotowin.core.domain.PasswordResetRequest
import com.gotowin.core.domain.RegisterRequest
import com.gotowin.core.domain.util.RandomUtil
import com.gotowin.persistance.GotowinUserEntity
import com.gotowin.persistance.repository.UserRepository
import com.gotowin.persistance.toBusinessModel
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate


@Service
class PersistentUserRepositoryAdapter(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val mailService: MailService,
    private val userContextService: UserContextService
): UserRepositoryAdapter    {

    private val logger = LoggerFactory.getLogger(PersistentUserRepositoryAdapter::class.java)

    @Transactional
    override fun registerUser(user: RegisterRequest): GotowinUserEntity {
        val encryptedPassword = passwordEncoder.encode(user.password)
        val newUser = if (user.referralCode != null && user.referralCode.length > 10) createUserByReferral(user, encryptedPassword)
        else createSimpleUser(user, encryptedPassword)
        userRepository.save(newUser)
        mailService.sendStandardEmail(newUser, SimpleMailSenderRequest.ACCOUNT_ACTIVATION.getModel(newUser))
        logger.debug("Created user: {}", newUser)
        return newUser
    }

    private fun createSimpleUser(user: RegisterRequest, encryptedPassword: String) = GotowinUserEntity(
        email = user.email,
        fullName = user.fullName,
        password = encryptedPassword,
        createdOn = LocalDate.now(),
        activated = false,
        activationKey = RandomUtil.generateActivationKey(),
        referralCode = RandomUtil.generateReferralCode()
    )

    private fun createUserByReferral(user: RegisterRequest, encryptedPassword: String): GotowinUserEntity {
        val referralUser =
            userRepository.findByReferralCode(user.referralCode!!) ?: throw ReferralCodeNotFoundException()
        referralUser.referralCount++

        val newUser = GotowinUserEntity(
            email = user.email,
            fullName = user.fullName,
            password = encryptedPassword,
            createdOn = LocalDate.now(),
            activated = false,
            activationKey = RandomUtil.generateActivationKey(),
            referralCode = RandomUtil.generateReferralCode(),
            referralUserId = referralUser.id
        )
        return newUser
    }

    override fun existByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    override fun findByEmailIgnoreCase(email: String): GotowinUserEntity? {
        return userRepository.findByEmailIgnoreCase(email)
    }

    override fun activateUser(key: String): GotowinUserEntity {
        val user = userRepository.findByActivationKey(key)
        if (user != null) {
            user.activated = true
            user.activationKey = null
            userRepository.save(user)
            mailService.sendStandardEmail(user, SimpleMailSenderRequest.WELCOME.getModel(user))
            return user
        } else { throw ActivationKeyNotFoundException() }
    }

    override fun getUser(): GotowinUser {
        val currentUser = userContextService.getCurrentUser()
        return currentUser.toBusinessModel()
    }

    override fun updateWalletAddress(walletAddress: String): GotowinUser {
        val user = userContextService.getCurrentUser()
        user.walletAddress = walletAddress
        userRepository.save(user)
        return user.toBusinessModel()
    }

    override fun changePassword(password: String) {
        val currentUser = userContextService.getCurrentUser()
        val newPassword = passwordEncoder.encode(password)
        currentUser.password = newPassword
        userRepository.save(currentUser)
        logger.debug("Changed password for User: {}", currentUser)
    }

    override fun requestPasswordReset(mail: String) {
        val user = userRepository.findByEmailIgnoreCase(mail) ?: throw EmailNotFoundException("Email address not registered")
        user.resetKey = RandomUtil.generateResetKey()
        userRepository.save(user)
        mailService.sendStandardEmail(user, SimpleMailSenderRequest.RESET_PASSWORD.getModel(user))
    }

    override fun completePasswordReset(passwordReset: PasswordResetRequest) {
        val user = userRepository.findByResetKey(passwordReset.key) ?: throw ResetKeyNotFoundException()
        val newPassword = if (passwordReset.newPassword == passwordReset.newPasswordConfirm) {
            passwordEncoder.encode(passwordReset.newPassword)
        } else { throw InvalidPasswordException("Passwords are not equals") }

        user.password = newPassword
        user.resetKey = null
        userRepository.save(user)

        logger.debug("Reset user password for reset key {}", passwordReset.key)
    }
}
