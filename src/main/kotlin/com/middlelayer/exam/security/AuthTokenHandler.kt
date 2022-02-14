package com.middlelayer.exam.security

import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("singleton")
class AuthTokenHandler {
    private var basicToken: String? = null

    fun getBasicToken(): String {
        return basicToken.let {
            it ?: ""
        }
    }

    fun setBasicToken(token: String) {
        basicToken = token
    }
}