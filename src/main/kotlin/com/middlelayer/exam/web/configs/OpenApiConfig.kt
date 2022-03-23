package com.middlelayer.exam.web.configs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    
    private val SCHEME_NAME = "Security Scheme"
    
    @Bean
    fun api(): OpenAPI {
        return OpenAPI()
            .addSecurityItem(SecurityRequirement().addList(SCHEME_NAME))
            .components(
                Components()
                    .addSecuritySchemes(SCHEME_NAME,
                        securityScheme()
                    ))
            .info(info())
    }

    private fun securityScheme(): SecurityScheme {
        var securityScheme = SecurityScheme()
        securityScheme.name = SCHEME_NAME
        securityScheme.bearerFormat = "JWT"
        securityScheme.type = SecurityScheme.Type.HTTP
        securityScheme.`in` = SecurityScheme.In.HEADER
        securityScheme.scheme = "Bearer"
        return securityScheme
    }

    private fun info(): Info {
        var info = Info()
        info.title = "Middlelayer"
        info.description = "Dev API Documentation"
        info. version = "v1"
        return info;
    }
}