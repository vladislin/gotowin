package com.gotowin.core.facade

import com.gotowin.core.adapter.WalletRepositoryAdapter
import com.gotowin.core.domain.Deposit
import org.springframework.http.ResponseEntity


class WalletFacade(private val walletRepositoryAdapter: WalletRepositoryAdapter) {
    fun calculatePrice(value: Int): Map<String, Float> = walletRepositoryAdapter.calculatePrice(value)

    fun deposit(customerIp: String, amount: Int): Deposit = walletRepositoryAdapter.createDeposit(customerIp, amount)

    fun callback(id: String, accountId: String) = walletRepositoryAdapter.callback(id, accountId)
}