package com.gotowin.core.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TransactionStatus(
    val response: TransactionStatusResponse
)


@JsonIgnoreProperties(ignoreUnknown = true)
data class TransactionStatusResponse(
    val status: Int,
)