package com.middlelayer.exam.web.configs

import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.web.filters.AuthFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity()
class WebSecurityConfig : WebSecurityConfigurerAdapter {

    private var authService: IAuthService

    @Autowired
    constructor(authService: IAuthService) {
        this.authService = authService
    }

    override fun configure(web: WebSecurity?) {
        super.configure(web)
    }

    override fun configure(http: HttpSecurity?) {
        http?.let {
            it.cors { cors ->
                cors.configurationSource(setCors())
            }
            it.addFilterAfter(AuthFilter(authService), CorsFilter::class.java)
            it.logout { logout ->
                logout.disable()
            }
            it.httpBasic { basic ->
                basic.disable()
            }
            it.csrf { csrf ->
                csrf.disable()
            }
        }
    }

    private fun setCors(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:6000")
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = false
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}