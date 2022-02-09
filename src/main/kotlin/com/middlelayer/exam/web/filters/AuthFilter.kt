package com.middlelayer.exam.web.filters

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.core.env.Environment
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthFilter : GenericFilterBean {

    private var filterUrl: RequestMatcher? = null
    private var secretKey: String = ""

    constructor(env: Environment) {
        secretKey = env.getProperty("jwt.secret.key", "")
        filterUrl = AntPathRequestMatcher("/user/**/profile")
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        var req = request as HttpServletRequest
        var res = response as HttpServletResponse
        var isAuthorized = true
        if (filterUrl?.matches(req) != true) {
            try {
                var token = req.getHeader("Authorization")
                if (token != null) {
                    token = token.replace("Bearer", "")
                    var jws = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                } else {
                    isAuthorized = false
                }
            } catch (ex: JwtException) {
                isAuthorized = false
            }
        }
        if (!isAuthorized) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access")
        }
        chain?.doFilter(req, res)
    }
}