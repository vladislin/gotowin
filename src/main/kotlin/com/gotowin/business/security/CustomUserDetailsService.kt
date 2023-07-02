package com.gotowin.business.security

import com.gotowin.core.adapter.UserRepositoryAdapter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class CustomUserDetailsService(private val userRepositoryAdapter: UserRepositoryAdapter): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepositoryAdapter.findByEmailIgnoreCase(username) ?: throw UsernameNotFoundException("User not found with username $username")
        val authorities = user.roles.map { SimpleGrantedAuthority(it.name) }
        if (user.hasAccess()) {
            return User(user.email, user.password, authorities)
        } else {
            error("User was $username not activated")
        }

    }
}