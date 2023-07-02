package com.gotowin.core.facade

import com.gotowin.core.domain.RegisterDTO
import com.gotowin.core.adapter.UserRepositoryAdapter
import com.gotowin.core.domain.PasswordReset


class UserFacade(private val userRepositoryAdapter: UserRepositoryAdapter) {

    fun registerUser(user: RegisterDTO) = userRepositoryAdapter.registerUser(user)

    fun existByEmail(email: String) = userRepositoryAdapter.existByEmail(email)

    fun activateUser(key: String) = userRepositoryAdapter.activateUser(key)

    fun getUser() = userRepositoryAdapter.getUser()

    fun changePassword(password: String) = userRepositoryAdapter.changePassword(password)

    fun requestPasswordReset(mail: String) = userRepositoryAdapter.requestPasswordReset(mail)

    fun completePasswordReset(passwordReset: PasswordReset) = userRepositoryAdapter.completePasswordReset(passwordReset)
}