package com.gotowin.core.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExternalTransactionStatus(
    @JsonProperty(value = "response") val response: ExternalTransactionStatusResponse
)


@JsonIgnoreProperties(ignoreUnknown = true)
data class ExternalTransactionStatusResponse(
    @JsonProperty(value = "status") val status: Int,
)

enum class TransactionStatus {
    NEW,
    SUCCESS,
    FAILED,
    CANCELLED,
    REVERSED,
    EXPIRED,
    HOLD,
    PENDING
}