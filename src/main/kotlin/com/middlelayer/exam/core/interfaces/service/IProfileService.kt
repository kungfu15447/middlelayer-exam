package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.Profile
import com.middlelayer.exam.core.models.Service

interface IProfileService {
    fun getProfile(authorization: String, userid: String): Profile
    fun getServicesFromProfile(basicAuthToken: String, userId: String): List<Service>
}