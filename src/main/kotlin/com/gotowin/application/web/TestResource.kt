package com.gotowin.application.web

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/healthcheck")
@Tag(name = "Test resource", description = "API for healthcheck of application")
class HealthCheckResource {
    @GetMapping
    fun healthCheck() = "Hello, GoToWin"
}