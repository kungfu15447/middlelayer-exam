package com.middlelayer.exam.web.filters

import com.middlelayer.exam.core.interfaces.service.IAuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
            res.status = HttpStatus.UNAUTHORIZED.value()
            res.outputStream.print("No token/invalid Bearer token in Authorization header")
            res.contentType = MediaType.APPLICATION_JSON_VALUE
            return
        }

        try {
            token = token.substring(7)
            authService.getClaimsFromJWTToken(token)
        } catch(ex: Exception) {
            res.status = HttpStatus.UNAUTHORIZED.value()
            res.outputStream.print("Invalid token. Unauthorized access")
            res.contentType = MediaType.APPLICATION_JSON_VALUE
            return
        }

        chain?.doFilter(req, res)
    }
}