package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.domain.DProfile
import com.middlelayer.exam.core.models.domain.DService

interface IAuthService {
    fun register(basicAuthToken: String, profile: DProfile, services: List<DService>): String
    fun createBasicAuthToken(username: String, password: String, withFormat: Boolean = false): String
}