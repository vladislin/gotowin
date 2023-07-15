package com.gotowin.core.facade

import com.gotowin.core.adapter.UserRepositoryAdapter
import com.gotowin.core.domain.CalculateRequest

class WalletFacade(private val userRepositoryAdapter: UserRepositoryAdapter) {
    fun calculatePrice(calculateRequest: CalculateRequest) = userRepositoryAdapter.calculatePrice(calculateRequest)
}