package com.gotowin.persistance.repository

import com.gotowin.persistance.GotowinUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID


interface UserRepository : JpaRepository<GotowinUserEntity, Long> {
    fun findByEmailIgnoreCase(email: String): GotowinUserEntity?
    fun findByActivationKey(key: String): GotowinUserEntity?
    fun existsByEmail(email: String): Boolean
    fun findByResetKey(resetKey: String): GotowinUserEntity?
    fun findByReferralCode(referralCode: String): GotowinUserEntity?
}