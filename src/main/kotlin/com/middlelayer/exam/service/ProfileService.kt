package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.core.models.xsi.Profile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import com.middlelayer.exam.core.models.xsi.Service as ServiceModel

@Service
class ProfileService : IProfileService {

    private val profileRepository: IProfileRepository

    @Autowired
    constructor(profileRepository: IProfileRepository) {
        this.profileRepository = profileRepository
    }
    override fun getProfile(authorization: String, userid: String): Mono<Profile> {
        return profileRepository.getProfileXsi(authorization, userid)
    }

    override fun getServicesFromProfile(basicAuthToken: String, userId: String): Mono<List<ServiceModel>> {
        return profileRepository.getServicesFromProfileXsi(basicAuthToken, userId)
    }
}