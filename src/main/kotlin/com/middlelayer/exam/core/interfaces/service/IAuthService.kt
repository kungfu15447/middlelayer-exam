package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.auth.TokenClaimsObject
import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service

interface IAuthService {
    fun register(basicAuthToken: String, profile: Profile, services: List<Service>): String
    fun createBasicAuthToken(username: String, password: String): String
    fun getClaimsFromJWTToken(token: String): TokenClaimsObject
}