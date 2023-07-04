package com.gotowin.application.configuration

import com.gotowin.business.security.JwtAuthEntryPoint
import com.gotowin.business.security.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableMethodSecurity
class WebSecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val jwtAuthEntryPoint: JwtAuthEntryPoint
) {
    companion object {
        const val BEARER_KEY_SECURITY_SCHEME = "bearer-key"
        const val USER_ROLE = "ROLE_USER"
    }
    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager =
        configuration.authenticationManager
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        http.authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET, "/api/healthcheck").authenticated()
            .requestMatchers(
                "/api/register",
                "/api/authenticate",
                "/api/activate/**",
                "/api/account/reset-password/init",
                "/api/account/reset-password/finish").permitAll()

            .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
            .anyRequest().authenticated()
        http.exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }
}