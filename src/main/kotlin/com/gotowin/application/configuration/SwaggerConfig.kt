package com.gotowin.application.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(Info().title("GoToWin API"))
    }

    @Bean
    fun customApi(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build()
    }
}