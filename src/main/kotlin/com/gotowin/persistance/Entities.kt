package com.gotowin.persistance

import com.fasterxml.jackson.annotation.JsonIgnore
import com.gotowin.core.domain.Deposit
import com.gotowin.core.domain.GotowinUser
import com.gotowin.core.domain.Transaction
import com.gotowin.core.domain.TransactionStatus
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "user")
data class GotowinUserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val fullName: String,

    @Column(length = 100, unique = true)
    val email: String,

    val createdOn: LocalDate,

    val referralCode: String,

    val referralUserId: Long? = null,

    // mutable variables
    @JsonIgnore
    var password: String,

    var activated: Boolean,

    var activationKey: String?,

    var resetKey: String? = null,

    var referralEarnedBalance: Float = 0F,

    var referralCount: Int = 0,

    var walletAddress: String? = null,

    var walletBalance: Float = 0F
) {
    fun hasAccess() = activated
}

@Entity
@Table(name = "deposit")
data class DepositEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    val externalTransactionId: Int,
    val amount: Int,
    val amountInCrypto: Float,
    val userId: Long,
    val paymentUrl: String,
    @Enumerated(EnumType.STRING) var status: TransactionStatus
)

// ----------------------------------------------------------------------------------------------------------------------


fun GotowinUserEntity.toBusinessModel(): GotowinUser {
    return GotowinUser(
        id = id!!,
        email = email,
        fullName = fullName,
        activated = activated,
        referralCode = referralCode,
        referralCount = referralCount,
        referralEarnedBalance = referralEarnedBalance,
        walletAddress = walletAddress ?: "",
        walletBalance = walletBalance
    )
}

fun DepositEntity.toBusinessEntity(): Deposit {
    return Deposit(
        amount = amount,
        amountInCrypto = amountInCrypto,
        paymentUrl = paymentUrl
    )
}

fun Transaction.toEntity(amountInCrypto: Float): DepositEntity {
    return DepositEntity(
        externalTransactionId = this.response.id,
        amount = this.response.amount.div(100),
        amountInCrypto = amountInCrypto,
        paymentUrl = this.response.result!!.payUrl,
        status = TransactionStatus.values().getOrNull(this.response.status) ?: TransactionStatus.NEW,
        userId = this.response.accountId.toLong()
    )
}
