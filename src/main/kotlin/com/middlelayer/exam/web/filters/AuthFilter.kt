package com.middlelayer.exam.web.filters

import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest





class AuthFilter : GenericFilterBean {

    var filterUrl: RequestMatcher? = null

    constructor() {
        filterUrl = AntPathRequestMatcher("/user/**/profile")
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val path = (request as HttpServletRequest).servletPath
        if (filterUrl?.matches(request) == true) {
            println("Creating token")
        } else {
            println("Checking token")
        }
        println(path)
        chain?.doFilter(request, response)
    }
}