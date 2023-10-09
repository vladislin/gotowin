package com.gotowin.core.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Transaction(
    val response: Response
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response(
    @JsonProperty(value = "external_customer_id") val accountId: String,
    val amount: Int,
    val id: Int,
    val result: Result? = null,
    val status: Int,
)

data class Result(
    @JsonProperty(value = "pay_url") val payUrl: String
)