package com.gotowin.application.configuration

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {}

    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        val converter = KotlinSerializationJsonHttpMessageConverter(Json {
            ignoreUnknownKeys = true
        })
        converter.supportedMediaTypes = converter.supportedMediaTypes + MediaType.TEXT_PLAIN
        converters.forEachIndexed { index, httpMessageConverter ->
            if (httpMessageConverter is KotlinSerializationJsonHttpMessageConverter) {
                converters[index] = converter
                return
            }
        }
    }
}