package com.gotowin.core.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TransactionStatus(
    @JsonProperty(value = "response") val response: TransactionStatusResponse
)


@JsonIgnoreProperties(ignoreUnknown = true)
data class TransactionStatusResponse(
    @JsonProperty(value = "status") val status: Int,
)