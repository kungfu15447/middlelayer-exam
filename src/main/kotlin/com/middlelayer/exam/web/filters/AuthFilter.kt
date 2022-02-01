package com.middlelayer.exam.web.filters

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.GenericFilterBean
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthFilter : GenericFilterBean {

    var filterUrl: RequestMatcher? = null

    constructor() {
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
                        .setSigningKey("cfd0209b-7a93-482f-97d0-cd9d368a5533")
                        .parseClaimsJws(token)
                    println(jws)
                } else {
                    isAuthorized = false
                }
            } catch (ex: JwtException) {
                isAuthorized = false
            }
        }
        if (!isAuthorized) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access")
        }else {
            chain?.doFilter(req, res)
        }
    }
}