package com.gotowin.persistance.adapter

import com.gotowin.application.configuration.ApplicationProperties
import com.gotowin.business.mail.MailService
import com.gotowin.business.security.UserContextService
import com.gotowin.core.adapter.WalletRepositoryAdapter
import com.gotowin.core.domain.Deposit
import com.gotowin.core.domain.TransactionStatus
import com.gotowin.core.domain.Transaction
import com.gotowin.core.domain.ExternalTransactionStatus
import com.gotowin.persistance.DepositEntity
import com.gotowin.persistance.GotowinUserEntity
import com.gotowin.persistance.repository.UserRepository
import com.gotowin.persistance.repository.WalletRepository
import com.gotowin.persistance.toBusinessEntity
import com.gotowin.persistance.toEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.math.BigInteger
import java.net.URI
import java.security.MessageDigest
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Service
class PersistentWalletRepositoryAdapter(
    private val restTemplate: RestTemplate,
    private val userContextService: UserContextService,
    private val applicationProperties: ApplicationProperties,
    private val walletRepository: WalletRepository,
    private val userRepository: UserRepository,
    private val mailService: MailService
) : WalletRepositoryAdapter {

    companion object {
        const val REFERRAL_PERCENT = 0.15f
    }

    private val logger = LoggerFactory.getLogger(PersistentWalletRepositoryAdapter::class.java)
    @Value("\${spring.mail.username}") private val toAddress: String = ""

    override fun calculatePrice(value: Int): Map<String, Float> {
        val convertedValue = (value * 25).toFloat()
        return mapOf("convertedValue" to convertedValue)
    }

    override fun createDeposit(customerIp: String, amount: Int): Deposit {
        val user: GotowinUserEntity = userContextService.getCurrentUser()
        val transaction: Transaction = createTransaction(user.id!!, customerIp, amount * 100)
        val amountInCrypto = calculatePrice(amount)
        val entity = walletRepository.save(transaction.toEntity(amountInCrypto.getValue("convertedValue")))
        return entity.toBusinessEntity()
    }

    @Transactional
    override fun callback(id: String, accountId: String) {
        val transactionStatus: TransactionStatus = getTransactionStatus(id)
        val updatedDeposit: DepositEntity = updateDepositStatus(id, accountId, transactionStatus)
        val updatedUser: GotowinUserEntity = updateUserBalance(accountId, updatedDeposit)
        updateReferrerBalance(updatedUser.referralUserId!!, updatedDeposit)
        mailService.sendDepositNotificationForAdmin(
            to = "vlad.slinchuk@gmail.com",
            subject = "Поповнення На Сайті",
            attributes = mapOf(
                "user" to updatedUser,
                "deposit" to updatedDeposit
            )
        )
    }

    private fun updateReferrerBalance(referralUserId: Long, deposit: DepositEntity) {
        val referrer: GotowinUserEntity? = userRepository.findById(referralUserId).orElse(null)
        val amountForReferral = deposit.amountInCrypto * REFERRAL_PERCENT
        if (referrer == null) {
          return
        }
        referrer.referralEarnedBalance = amountForReferral
        userRepository.save(referrer)
    }

    private fun updateDepositStatus(id : String, accountId: String, transactionStatus: TransactionStatus): DepositEntity {
        val depositEntity: DepositEntity = walletRepository.findByExternalTransactionIdAndUserId(id.toInt(), accountId.toLong())
        depositEntity.status = transactionStatus
        walletRepository.save(depositEntity)
        return depositEntity
    }
    private fun updateUserBalance(accountId: String, depositEntity: DepositEntity): GotowinUserEntity {
        val userEntity = userRepository.findById(accountId.toLong()).orElse(null)
        userEntity.walletBalance += depositEntity.amountInCrypto
        userRepository.save(userEntity)
        return userEntity
    }
    private fun getTransactionStatus(id: String): TransactionStatus {
        val request = mapOf("auth" to getCredentials(), "id" to id)
        val response = restTemplate.exchange(
            URI("${applicationProperties.payonhostUri}/transaction/find"),
            HttpMethod.POST,
            HttpEntity(request),
            ExternalTransactionStatus::class.java
        )
        return when (response.body!!.response.status) {
            0 -> TransactionStatus.NEW
            1 -> TransactionStatus.SUCCESS
            2 -> TransactionStatus.FAILED
            3 -> TransactionStatus.CANCELLED
            4 -> TransactionStatus.REVERSED
            5 -> TransactionStatus.EXPIRED
            6 -> TransactionStatus.HOLD
            else -> TransactionStatus.PENDING
        }
    }
    private fun createTransaction(userId: Long, customerIp: String, amount: Int): Transaction {
        val request = mapOf(
            "auth" to getCredentials(),
            "locale" to "en",
            "external_customer_id" to "$userId",
            "customer_ip_address" to customerIp,
            "amount" to amount,
            "amount_currency" to "UAH",
            "service_id" to applicationProperties.payonhostServiceId,
            "account_id" to applicationProperties.payonhostAccountId,
            "wallet_id" to applicationProperties.payonhostWalletId,
//            "point" to mapOf(
//                "callback_url" to "https://gotowin.co/api/wallet/callback/{transaction_id}?customer={external_customer_id}",
//                "success_url" to "https://gotowin.co/profile",
//                "fail_url" to "https://gotowin.co/"
//            )
        )
        val response = restTemplate.exchange(
            URI("${applicationProperties.payonhostUri}/transaction/create"),
            HttpMethod.POST,
            HttpEntity(request),
            Transaction::class.java
        )
        return response.body!!

    }
    private fun getCredentials(): Map<String, Any> {
        val key = Instant.now().epochSecond
        return mapOf(
            "point" to applicationProperties.payonhostPointId,
            "key" to key,
            "hash" to calculateHash(key)
        )
    }
    private fun calculateHash(key: Long): String {
        val md = MessageDigest.getInstance("MD5")
        val input = "${applicationProperties.payonhostPointId}${applicationProperties.payonhostApiToken}$key"
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}