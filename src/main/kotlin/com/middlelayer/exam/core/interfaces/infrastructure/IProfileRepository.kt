package com.middlelayer.exam.core.interfaces.infrastructure
import com.middlelayer.exam.core.models.Profile
import com.middlelayer.exam.core.models.Service

interface IProfileRepository {
    fun getProfileXsi(basicAuthToken: String, userId: String): Profile
    fun getServicesFromProfileXsi(basicAuthToken: String, userId: String): List<Service>
}