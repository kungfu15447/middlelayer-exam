package com.middlelayer.exam.web.filters

import com.middlelayer.exam.core.interfaces.service.IAuthService
import org.springframework.beans.factory.annotation.Autowired
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

    private var whitelistedUrls: RequestMatcher
    private var authService: IAuthService

    @Autowired
    constructor(authService: IAuthService) {
        this.authService = authService
        whitelistedUrls = OrRequestMatcher(
            AntPathRequestMatcher("/api/user/profile/login"),
            AntPathRequestMatcher("/v3/api-docs/**"),
            AntPathRequestMatcher("/swagger-ui/**"),
            AntPathRequestMatcher("/swagger-ui.html"),
        )
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        var req = request as HttpServletRequest
        var res = response as HttpServletResponse

        if (whitelistedUrls.matches(req)) {
            chain?.doFilter(req, res)
            return
        }

        var token = req.getHeader("Authorization")

        if (token == null || !token.startsWith("Bearer ")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token/invalid Bearer token in Authorization header")
            return
        }

        try {
            token = token.substring(7)
            authService.getClaimsFromJWTToken(token)
        } catch(ex: Exception) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token. Unauthorized access")
        }

        chain?.doFilter(req, res)
    }
}