package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service
import reactor.core.publisher.Mono

interface IProfileService {
    fun getProfile(authorization: String, userid: String): Mono<Profile>
    fun getServicesFromProfile(basicAuthToken: String, userId: String): Mono<List<Service>>
}