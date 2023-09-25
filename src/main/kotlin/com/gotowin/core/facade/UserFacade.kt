package com.gotowin.core.facade

import com.gotowin.core.domain.RegisterRequest
import com.gotowin.core.adapter.UserRepositoryAdapter
import com.gotowin.core.domain.PasswordResetRequest


class UserFacade(private val userRepositoryAdapter: UserRepositoryAdapter) {
    fun registerUser(user: RegisterRequest) = userRepositoryAdapter.registerUser(user)
    fun existByEmail(email: String) = userRepositoryAdapter.existByEmail(email)
    fun activateUser(key: String) = userRepositoryAdapter.activateUser(key)
    fun getUser() = userRepositoryAdapter.getUser()
    fun updateWalletAddress(walletAddress: String) = userRepositoryAdapter.updateWalletAddress(walletAddress)
    fun changePassword(password: String) = userRepositoryAdapter.changePassword(password)
    fun requestPasswordReset(mail: String) = userRepositoryAdapter.requestPasswordReset(mail)
    fun completePasswordReset(passwordReset: PasswordResetRequest) = userRepositoryAdapter.completePasswordReset(passwordReset)
}