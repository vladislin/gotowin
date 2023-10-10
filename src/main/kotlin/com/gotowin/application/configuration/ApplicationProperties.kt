package com.gotowin.application.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class ApplicationProperties(
    @Value("\${hostname}") val host: String,
    @Value("\${payonhost_uri}") val payonhostUri: String,
    @Value("\${payonhost_account_id}") val payonhostAccountId: String,
    @Value("\${payonhost_wallet_id}") val payonhostWalletId: String,
    @Value("\${payonhost_point_id}") val payonhostPointId: Int,
    @Value("\${payonhost_service_id}") val payonhostServiceId: String,
    @Value("\${payonhost_api_token}") val payonhostApiToken: String,
)