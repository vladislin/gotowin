package com.gotowin.core.adapter

import com.gotowin.core.domain.DepositResponse

interface WalletRepositoryAdapter {
    fun calculatePrice(value: Int): Map<String, Float>
    fun deposit(customerIp: String, amount: Int): DepositResponse
}