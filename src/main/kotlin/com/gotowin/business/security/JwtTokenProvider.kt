package com.gotowin.business.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenProvider {

    @Value("\${app.jwt-secret}")
    private val jwtSecret: String? = null

    @Value("\${app-jwt-expiration-milliseconds}")
    private val jwtExpirationDate: Long = 0

    fun generateToken(authentication: Authentication): String {
        val username = authentication.name

        val currentDate = Date()
        val expireDate = Date(currentDate.time + jwtExpirationDate)

        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(expireDate)
            .signWith(key())
            .compact()
    }
    fun getUsername(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key())
            .build()
            .parseClaimsJws(token)
            .body

        val username = claims.subject

        return username
    }
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(token)
            return true
        } catch (e: MalformedJwtException) {
            throw Exception("Invalid JWT token")
        } catch (e: ExpiredJwtException) {
            throw Exception("Expired JWT token")
        } catch (e: UnsupportedJwtException) {
            throw Exception("Unsupported JWT token")
        } catch (e: IllegalArgumentException) {
            throw Exception("JWT claims string is empty.")
        }
    }
    private fun key() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret))
}