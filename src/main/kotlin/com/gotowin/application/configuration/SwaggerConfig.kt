package com.gotowin.application.configuration

import com.gotowin.application.configuration.WebSecurityConfig.Companion.BEARER_KEY_SECURITY_SCHEME
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .components(
                Components()
                    .addSecuritySchemes(BEARER_KEY_SECURITY_SCHEME,
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .`in`(SecurityScheme.In.HEADER)
                    )
            )
            .info(Info().title("GoToWin API"))
    }

    @Bean
    fun customApi(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build()
    }
}