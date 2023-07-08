package com.gotowin.persistance

import com.fasterxml.jackson.annotation.JsonIgnore
import com.gotowin.core.domain.GotowinUser
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDate
import java.util.*


// TODO: Позбутися ролей, вони тут не потрібні

@Entity
@Table(name = "user")
data class GotowinUserEntity(

    @Id @JdbcTypeCode(SqlTypes.VARCHAR)
    val id: UUID,

    val fullName: String,

    @Column(length = 100, unique = true)
    val email: String,

    val createdOn: LocalDate,

    val referralCode: String,

    val referralUserId: UUID? = null,

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

// ----------------------------------------------------------------------------------------------------------------------


fun GotowinUserEntity.toBusinessModel(): GotowinUser {
    return GotowinUser(
        id = id,
        email = email,
        fullName = fullName,
        activated = activated,
        referralCode = referralCode,
        referralEarnedBalance = referralEarnedBalance,
        walletAddress = walletAddress ?: "",
        walletBalance = walletBalance
    )
}
