package com.gotowin.persistance.repository

import com.gotowin.persistance.GotowinUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID


interface UserRepository : JpaRepository<GotowinUserEntity, UUID> {
    fun findByEmailIgnoreCase(email: String): GotowinUserEntity?
    fun findByActivationKey(key: String): GotowinUserEntity?
    fun existsByEmail(email: String): Boolean
    fun findByResetKey(resetKey: String): GotowinUserEntity?
}