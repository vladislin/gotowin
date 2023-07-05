package com.gotowin.application.web

import com.gotowin.application.configuration.WebSecurityConfig
import com.gotowin.business.mail.MailService
import com.gotowin.business.security.JwtTokenProvider
import com.gotowin.business.security.UserContextService
import com.gotowin.core.domain.*
import com.gotowin.core.facade.UserFacade
import com.gotowin.persistance.toBusinessModel
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api")
@Tag(name = "Auth resource", description = "API for work with authentication")
@Secured(WebSecurityConfig.USER_ROLE)
@SecurityRequirement(name = WebSecurityConfig.BEARER_KEY_SECURITY_SCHEME)
class AuthResource(
    private val userFacade: UserFacade,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userContextService: UserContextService
) {
    @PostMapping("/authenticate")
    fun authenticateUser(@RequestBody authenticateDTO: AuthenticateDTO): ResponseEntity<AuthenticateResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authenticateDTO.email, authenticateDTO.password))
        SecurityContextHolder.getContext().authentication = authentication
        println(SecurityContextHolder.getContext().authentication)

        val token = jwtTokenProvider.generateToken(authentication)
        val user = userContextService.getCurrentUser().toBusinessModel()
        val authenticateResponse = AuthenticateResponse(user, token)
        return ResponseEntity(authenticateResponse, HttpStatus.OK)
    }
    @PostMapping("/register")
    fun registerUser(@RequestBody registerDTO: RegisterDTO): ResponseEntity<*> {
        if (registerDTO.password != registerDTO.confirmPassword) {
            return ResponseEntity("Passwords are not equals!", HttpStatus.BAD_REQUEST)
        }
        if (userFacade.existByEmail(registerDTO.email)) {
            return ResponseEntity("User already exist!", HttpStatus.BAD_REQUEST)
        }
        userFacade.registerUser(registerDTO)
        return ResponseEntity("User registered successfully!", HttpStatus.CREATED)
    }
    @GetMapping("/activate")
    fun activateAccount(@RequestParam key: String) {
        userFacade.activateUser(key)
    }
    @GetMapping("/account")
    fun getAccount(): GotowinUser {
        return userFacade.getUser()
    }
    @PostMapping("/account")
    fun saveAccount(@RequestBody user: GotowinUser) {
        TODO("Зробити апдейт юзера")
    }
    @PostMapping("/account/change-password")
    fun changePassword(@RequestBody changePassword: ChangePassword): ResponseEntity<*> {
        if (changePassword.password1 != changePassword.password2) {
            return ResponseEntity("Passwords are not equals", HttpStatus.BAD_REQUEST)
        }
        userFacade.changePassword(changePassword.password1)
        return ResponseEntity("Password changed", HttpStatus.OK)
    }
    @PostMapping("/account/reset-password/init")
    fun requestPasswordReset(@RequestParam mail: String) {
        userFacade.requestPasswordReset(mail)
    }
    @PostMapping("/account/reset-password/finish")
    fun finishPasswordReset(@RequestBody passwordReset: PasswordReset) {
        userFacade.completePasswordReset(passwordReset)
    }
}