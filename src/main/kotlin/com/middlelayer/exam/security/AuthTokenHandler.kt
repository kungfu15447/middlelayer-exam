package com.middlelayer.exam.security

import org.springframework.stereotype.Component

@Component
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