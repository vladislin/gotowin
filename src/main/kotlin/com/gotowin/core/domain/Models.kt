package com.gotowin.core.domain


data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val referralCode: String? = null
)
data class AuthenticateRequest(val email: String, val password: String)
data class JWTToken(
    val idToken: String
)
// TODO: не закінчено
data class GotowinUser(
    val id: Long,
    val email: String,
    val fullName: String,
    val activated: Boolean,
    val referralCode: String,
    val referralCount: Int,
    val referralEarnedBalance: Float,
    val walletAddress: String,
    val walletBalance: Float,
)
data class ChangePasswordRequest(
    val password1: String,
    val password2: String
)
data class PasswordResetRequest(
    val key: String,
    val newPassword: String,
    val newPasswordConfirm: String
)

data class WalletAddressUpdate(
    val walletAddress: String
)

data class CalculateRequest(
    val valueToConvert: Int
)

data class DepositResponse(val amount: Int)