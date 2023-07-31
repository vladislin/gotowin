package com.gotowin.business.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<ErrorMessage> {
        val errorMsg = ErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.message)
        return ResponseEntity(errorMsg, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleEmailNotFoundException(ex: EmailNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMsg = ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message)
        return ResponseEntity(errorMsg, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleInvalidPasswordException(ex: InvalidPasswordException): ResponseEntity<ErrorMessage> {
        val errorMsg = ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message)
        return ResponseEntity(errorMsg, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleResetKeyNotFoundException(ex: ResetKeyNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMsg = ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message)
        return ResponseEntity(errorMsg, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleActivationKeyNotFoundException(ex: ActivationKeyNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMsg = ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message)
        return ResponseEntity(errorMsg, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleReferralCodeNotFoundException(ex: ReferralCodeNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMsg = ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.message)
        return ResponseEntity(errorMsg, HttpStatus.NOT_FOUND)
    }
}
