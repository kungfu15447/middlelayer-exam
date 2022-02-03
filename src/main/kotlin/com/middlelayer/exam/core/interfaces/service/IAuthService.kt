package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.Profile
import com.middlelayer.exam.core.models.Service

interface IAuthService {
    fun register(basicAuthToken: String, profile: Profile, services: List<Service>): String
}