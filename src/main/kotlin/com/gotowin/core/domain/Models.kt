package com.gotowin.core.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID


data class RegisterDTO(
    val fullName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val referralCode: String?
)

data class AuthenticateDTO(val email: String, val password: String)

data class JWTToken(
    @JsonProperty("id_token") val idToken: String
)


// TODO: не закінчено
data class GotowinUser(
    val id: UUID,
    val email: String,
    val fullName: String,
    val activated: Boolean,
    val referralCode: String,
    val referralEarnedBalance: Float,
    val walletAddress: String,
    val walletBalance: Float
)

data class ChangePassword(
    val password1: String,
    val password2: String
)

data class PasswordReset(
    val key: String,
    val newPassword: String,
    val newPasswordConfirm: String
)