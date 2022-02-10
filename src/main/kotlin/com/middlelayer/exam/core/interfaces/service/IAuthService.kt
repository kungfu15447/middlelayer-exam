package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.domain.DProfile
import com.middlelayer.exam.core.models.domain.DService
import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service

interface IAuthService {
    fun register(basicAuthToken: String, profile: DProfile, services: List<DService>): String
}