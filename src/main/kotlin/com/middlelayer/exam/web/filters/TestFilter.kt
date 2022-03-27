package com.middlelayer.exam.web.filters

import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse


class TestFilter : GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        println("Processing saying hello...")
        chain?.doFilter(request, response)
        println("Hello!")
    }
}