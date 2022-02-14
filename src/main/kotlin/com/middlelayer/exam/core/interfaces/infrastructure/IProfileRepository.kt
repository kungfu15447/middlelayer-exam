package com.middlelayer.exam.core.interfaces.infrastructure
import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service
import reactor.core.publisher.Mono

interface IProfileRepository {
    fun getProfileXsi(userId: String): Mono<Profile>
    fun getServicesFromProfileXsi(userId: String): Mono<List<Service>>
}