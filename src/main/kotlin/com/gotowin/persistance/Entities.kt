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

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
    val roles: List<Role>,

    var activated: Boolean,

    var activationKey: String?,

    var resetKey: String? = null
) {
    fun hasAccess() = activated
}


@Entity
@Table(name = "role")
data class Role (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(length = 60)
    val name: String
)

// ----------------------------------------------------------------------------------------------------------------------


fun GotowinUserEntity.toBusinessModel(): GotowinUser {
    return GotowinUser(
        id = id,
        email = email,
        fullName = fullName,
        activated = activated
    )
}
