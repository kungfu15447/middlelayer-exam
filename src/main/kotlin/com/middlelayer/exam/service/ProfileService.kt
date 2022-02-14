package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.core.models.domain.DProfile
import com.middlelayer.exam.core.models.domain.DService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProfileService : IProfileService {

    private val profileRepository: IProfileRepository

    @Autowired
    constructor(profileRepository: IProfileRepository) {
        this.profileRepository = profileRepository
    }
    override fun getProfile(userid: String): Mono<DProfile> {
        return profileRepository.getProfileXsi(userid).flatMap {
            Mono.just(DProfile(it))
        }
    }

    override fun getServicesFromProfile(userId: String): Mono<List<DService>> {
        return profileRepository.getServicesFromProfileXsi(userId).flatMap { services ->
            Mono.just(services.map { service ->
                DService(service)
            })
        }
    }
}