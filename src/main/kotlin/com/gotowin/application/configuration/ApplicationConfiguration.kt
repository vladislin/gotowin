package com.gotowin.application.configuration

import com.gotowin.core.adapter.UserRepositoryAdapter
import com.gotowin.core.adapter.WalletRepositoryAdapter
import com.gotowin.core.facade.UserFacade
import com.gotowin.core.facade.WalletFacade
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
class ApplicationConfiguration {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
    @Bean
    fun userFacade(userRepositoryAdapter: UserRepositoryAdapter) = UserFacade(userRepositoryAdapter = userRepositoryAdapter)
    @Bean
    fun walletFacade(walletRepositoryAdapter: WalletRepositoryAdapter) = WalletFacade(walletRepositoryAdapter = walletRepositoryAdapter)
}