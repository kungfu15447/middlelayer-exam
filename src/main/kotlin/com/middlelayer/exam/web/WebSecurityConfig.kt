package com.middlelayer.exam.web

import com.middlelayer.exam.web.filters.AuthFilter
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        http?.cors {
            it.and()
                ?.addFilterAfter(AuthFilter(), BasicAuthenticationFilter::class.java)
        }
    }
}