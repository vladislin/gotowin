package com.gotowin.core.adapter

import com.gotowin.core.domain.Deposit
import org.springframework.http.ResponseEntity

interface WalletRepositoryAdapter {
    fun calculatePrice(value: Int): Map<String, Float>
    fun createDeposit(customerIp: String, amount: Int): Deposit
    fun callback(id: String, accountId: String): ResponseEntity<String>
}