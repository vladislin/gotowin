package com.gotowin.core.adapter

import com.gotowin.core.domain.GotowinUser
import com.gotowin.core.domain.PasswordReset
import com.gotowin.core.domain.RegisterDTO
import com.gotowin.persistance.GotowinUserEntity


interface UserRepositoryAdapter {
    fun registerUser(user: RegisterDTO): GotowinUserEntity
    fun existByEmail(email: String): Boolean
    fun findByEmailIgnoreCase(email: String): GotowinUserEntity?
    fun activateUser(key: String): GotowinUserEntity
    fun getUser(): GotowinUser
    fun updateWalletAddress(walletAddress: String): GotowinUser
    fun changePassword(password: String)
    fun requestPasswordReset(mail: String)
    fun completePasswordReset(passwordReset: PasswordReset)
}