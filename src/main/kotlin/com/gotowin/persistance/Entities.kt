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

    @Column
    @JsonIgnore
    var password: String,

    val createdOn: LocalDate,

    var activated: Boolean,

    var activationKey: String?,

    var resetKey: String? = null,

    val referralCode: String,

    val referralUserId: UUID? = null
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
        referralCode = referralCode
    )
}
