package com.gotowin.application.web

import com.gotowin.application.configuration.ApplicationProperties
import com.gotowin.application.configuration.WebSecurityConfig
import com.gotowin.core.domain.Deposit
import com.gotowin.core.facade.WalletFacade
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.ServletWebRequest

@RestController
@RequestMapping("/api/wallet")
@Tag(name = "Wallet resource", description = "API for work with wallet")
@Secured(WebSecurityConfig.USER_ROLE)
@SecurityRequirement(name = WebSecurityConfig.BEARER_KEY_SECURITY_SCHEME)
class WalletResource(
    private val walletFacade: WalletFacade,
    private val applicationProperties: ApplicationProperties
) {
    private val logger = LoggerFactory.getLogger(WalletResource::class.java)

    @GetMapping("/calculate-price/{value}")
    fun getCalculatedPrice(@PathVariable value: Int): Map<String, Float> {
        return walletFacade.calculatePrice(value)
    }

    @PostMapping("/deposit")
    fun deposit(request: HttpServletRequest, @RequestParam amount: Int): Deposit {
        return walletFacade.deposit(request.remoteAddr, amount)
    }

    @GetMapping("/callback/{external_transaction_id}")
    fun callback(
        @PathVariable("external_transaction_id") externalTransactionId: String,
        @RequestParam(name = "customer") accountId: String,
        request: ServletWebRequest
    ): ResponseEntity<String> {
        logger.info("Host: ${applicationProperties.host}, URI: ${request.request.requestURI}")
        val callbackResult = walletFacade.callback(externalTransactionId, accountId)
        return if (callbackResult != null) ResponseEntity.ok().body("") else ResponseEntity.badRequest().body("")
    }

}