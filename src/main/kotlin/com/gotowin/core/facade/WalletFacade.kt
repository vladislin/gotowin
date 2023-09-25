package com.gotowin.core.facade

import com.gotowin.core.adapter.WalletRepositoryAdapter
import com.gotowin.core.domain.DepositResponse


class WalletFacade(private val walletRepositoryAdapter: WalletRepositoryAdapter) {
    fun calculatePrice(value: Int): Map<String, Float> = walletRepositoryAdapter.calculatePrice(value)

    fun deposit(customerIp: String, amount: Int): DepositResponse = walletRepositoryAdapter.deposit(customerIp, amount)
}