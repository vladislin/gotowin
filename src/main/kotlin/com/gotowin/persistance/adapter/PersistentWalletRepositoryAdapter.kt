package com.gotowin.persistance.adapter

import com.gotowin.core.adapter.WalletRepositoryAdapter
import com.gotowin.core.domain.DepositResponse
import org.springframework.stereotype.Service

@Service
class PersistentWalletRepositoryAdapter : WalletRepositoryAdapter {
    override fun calculatePrice(value: Int): Map<String, Float> {
        val convertedValue = (value * 1000).toFloat()
        return mapOf("convertedValue" to convertedValue)
    }

    override fun deposit(customerIp: String, amount: Int): DepositResponse {
        TODO("Not yet implemented")
    }
}