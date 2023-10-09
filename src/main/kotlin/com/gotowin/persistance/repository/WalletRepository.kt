package com.gotowin.persistance.repository

import com.gotowin.persistance.DepositEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WalletRepository : JpaRepository<DepositEntity, Long> {
    fun findByExternalTransactionIdAndUserId(externalTransactionalId: Int, userId: Long): DepositEntity
}