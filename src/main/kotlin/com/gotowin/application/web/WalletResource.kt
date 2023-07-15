package com.gotowin.application.web

import com.gotowin.application.configuration.WebSecurityConfig
import com.gotowin.core.domain.CalculateRequest
import com.gotowin.core.facade.UserFacade
import com.gotowin.core.facade.WalletFacade
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/wallet")
@Tag(name = "Wallet resource", description = "API for work with wallet")
@Secured(WebSecurityConfig.USER_ROLE)
@SecurityRequirement(name = WebSecurityConfig.BEARER_KEY_SECURITY_SCHEME)
class WalletResource(
    private val walletFacade: WalletFacade
) {
    @GetMapping("/calculate-price/{value}")
    fun getCalculatedPrice(@PathVariable value: Int): Map<String, Float> {
        return walletFacade.calculatePrice(value)
    }

    @PostMapping("/deposit")
    fun deposit() {
        TODO()
    }

}