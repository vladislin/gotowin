package com.gotowin.application.web

import com.gotowin.application.configuration.WebSecurityConfig
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
@Tag(name = "Test resource", description = "API for healthcheck of application")
@Secured(WebSecurityConfig.USER_ROLE)
@SecurityRequirement(name = WebSecurityConfig.BEARER_KEY_SECURITY_SCHEME)
class HealthCheckResource {
    @GetMapping("/healthcheck")
    fun healthCheck() = "Hello, GoToWin"
}