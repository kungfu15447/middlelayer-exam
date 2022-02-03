package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.core.models.xsi.Profile
import org.springframework.stereotype.Service
import com.middlelayer.exam.core.models.xsi.Service as ServiceModel

@Service
class ProfileService : IProfileService {

    private val profileRepository: IProfileRepository

    constructor(profileRepository: IProfileRepository) {
        this.profileRepository = profileRepository
    }
    override fun getProfile(authorization: String, userid: String): Profile {
        return profileRepository.getProfileXsi(authorization, userid)
    }

    override fun getServicesFromProfile(basicAuthToken: String, userId: String): List<ServiceModel> {
        return profileRepository.getServicesFromProfileXsi(basicAuthToken, userId)
    }
}