package com.gotowin.business.exception

import org.springframework.http.HttpStatus

data class ErrorMessage(
    val status: Int? = null,
    val message: String? = null
)
