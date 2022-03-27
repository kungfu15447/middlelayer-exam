package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service as xsiService
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
    override fun getProfile(token: String, userid: String): Mono<Profile> {
        return profileRepository.getProfileXsi(token, userid)
        }

    override fun getServicesFromProfile(token: String, userId: String): Mono<List<xsiService>> {
        return profileRepository.getServicesFromProfileXsi(token, userId)
    }
}