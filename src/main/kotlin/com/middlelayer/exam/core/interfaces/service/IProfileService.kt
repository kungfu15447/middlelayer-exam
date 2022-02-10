package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.domain.DProfile
import com.middlelayer.exam.core.models.domain.DService
import reactor.core.publisher.Mono

interface IProfileService {
    fun getProfile(authorization: String, userid: String): Mono<DProfile>
    fun getServicesFromProfile(basicAuthToken: String, userId: String): Mono<List<DService>>
}