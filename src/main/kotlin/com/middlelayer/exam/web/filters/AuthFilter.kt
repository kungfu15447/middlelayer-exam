package com.middlelayer.exam.web.filters

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.core.env.Environment
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class AuthFilter : GenericFilterBean {

    private var whitelisted_urls: RequestMatcher
    private var secretKey: String = ""

    constructor(env: Environment) {
        secretKey = env.getProperty("jwt.secret.key", "")
        whitelisted_urls = OrRequestMatcher(
            AntPathRequestMatcher("/api/user/profile/login"),
            AntPathRequestMatcher("/v3/api-docs/**"),
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/swagger-ui.html"),
        )
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        var req = request as HttpServletRequest
        var res = response as HttpServletResponse

        if (whitelisted_urls.matches(req)) {
            chain?.doFilter(req, res)
            return
        }

        var token = req.getHeader("Authorization")

        if (token == null || !token.startsWith("Bearer ")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token/invalid Bearer token in Authorization header")
        }

        try {
            token = token.substring(7)
            var jws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
        } catch(ex: JwtException) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Could not decrypt token with secret key. Unauthorized access")
        }

        chain?.doFilter(req, res)
    }
}