package com.gotowin.business.security

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException


@Component
class JwtAuthEntryPoint : AuthenticationEntryPoint {

    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest?, response: HttpServletResponse, authException: AuthenticationException) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message)
    }
}