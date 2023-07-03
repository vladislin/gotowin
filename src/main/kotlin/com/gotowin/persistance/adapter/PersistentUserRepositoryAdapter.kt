package com.gotowin.persistance.adapter

import com.gotowin.business.mail.MailService
import com.gotowin.business.mail.SimpleMailSenderRequest
import com.gotowin.business.security.UserContextService
import com.gotowin.core.adapter.UserRepositoryAdapter
import com.gotowin.core.domain.GotowinUser
import com.gotowin.core.domain.PasswordReset
import com.gotowin.core.domain.RegisterDTO
import com.gotowin.core.domain.util.RandomUtil
import com.gotowin.persistance.GotowinUserEntity
import com.gotowin.persistance.Role
import com.gotowin.persistance.repository.UserRepository
import com.gotowin.persistance.toBusinessModel
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*


@Service
class PersistentUserRepositoryAdapter(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val mailService: MailService,
    private val userContextService: UserContextService
): UserRepositoryAdapter    {

    private val logger = LoggerFactory.getLogger(PersistentUserRepositoryAdapter::class.java)

    @Transactional
    override fun registerUser(user: RegisterDTO): GotowinUserEntity {
        val encryptedPassword = passwordEncoder.encode(user.password)
        val newUser = createReferralOrParentUser(user, encryptedPassword)
        userRepository.save(newUser)
        mailService.sendStandardEmail(newUser, SimpleMailSenderRequest.ACCOUNT_ACTIVATION.getModel(newUser))
        logger.debug("Created user: {}", newUser)
        return newUser
    }

    private fun createReferralOrParentUser(user: RegisterDTO, encryptedPassword: String): GotowinUserEntity {
        val newUser = if (user.referralCode != null) {
            val referralUserId = userRepository.findByReferralCode(user.referralCode)?.id
                ?: throw Exception("No user with this referral code")
            GotowinUserEntity(
                id = UUID.randomUUID(),
                email = user.email,
                fullName = user.fullName,
                password = encryptedPassword,
                createdOn = LocalDate.now(),
                roles = listOf(Role(name = "ROLE_USER")),
                activated = false,
                activationKey = RandomUtil.generateActivationKey(),
                referralCode = RandomUtil.generateReferralCode(),
                referralUserId = referralUserId
            )
        } else {
            GotowinUserEntity(
                id = UUID.randomUUID(),
                email = user.email,
                fullName = user.fullName,
                password = encryptedPassword,
                createdOn = LocalDate.now(),
                roles = listOf(Role(name = "ROLE_USER")),
                activated = false,
                activationKey = RandomUtil.generateActivationKey(),
                referralCode = RandomUtil.generateReferralCode()
            )
        }
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
        } else {
            throw Exception("No user was found for this reset key")
        }
    }

    override fun getUser(): GotowinUser {
        val currentUser = userContextService.getCurrentUser()
        return currentUser.toBusinessModel()
    }

    override fun changePassword(password: String) {
        val currentUser = userContextService.getCurrentUser()
        val newPassword = passwordEncoder.encode(password)
        currentUser.password = newPassword
        userRepository.save(currentUser)
        logger.debug("Changed password for User: {}", currentUser)
    }

    override fun requestPasswordReset(mail: String) {
        val user = userRepository.findByEmailIgnoreCase(mail) ?: throw Exception("No user was found with this email")
        user.resetKey = RandomUtil.generateResetKey()
        userRepository.save(user)
        mailService.sendStandardEmail(user, SimpleMailSenderRequest.RESET_PASSWORD.getModel(user))
    }

    override fun completePasswordReset(passwordReset: PasswordReset) {
        val user = userRepository.findByResetKey(passwordReset.key) ?: throw Exception("No user was found for this reset key")
        val newPassword = passwordEncoder.encode(passwordReset.newPassword)

        user.password = newPassword
        user.resetKey = null
        userRepository.save(user)

        logger.debug("Reset user password for reset key {}", passwordReset.key)
    }
}