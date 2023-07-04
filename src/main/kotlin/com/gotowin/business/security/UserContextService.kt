package com.gotowin.business.security

import com.gotowin.persistance.GotowinUserEntity
import com.gotowin.persistance.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service


@Service
@Transactional
class UserContextService(private val userRepository: UserRepository) {
    fun getCurrentUser(): GotowinUserEntity {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val username = if (principal is UserDetails) principal.username else principal.toString()

        return userRepository.findByEmailIgnoreCase(username) ?: throw RuntimeException("User not found")
    }
}