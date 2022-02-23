package com.middlelayer.exam.web.configs

import com.middlelayer.exam.web.filters.AuthFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity()
class WebSecurityConfig : WebSecurityConfigurerAdapter {

    private var env: Environment
    @Autowired
    constructor(env: Environment) {
        this.env = env
    }

    override fun configure(web: WebSecurity?) {
        web
            ?.ignoring()
            ?.antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/api/user/profile/login")
    }

    override fun configure(http: HttpSecurity?) {
        http
            ?.httpBasic()?.disable()
            ?.csrf()?.disable()
            ?.cors()?.configurationSource(setCors())
            ?.and()
            ?.authorizeRequests()
            ?.antMatchers("api/**")
            ?.permitAll()
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