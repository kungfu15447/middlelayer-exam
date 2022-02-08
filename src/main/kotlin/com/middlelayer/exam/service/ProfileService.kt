package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.core.models.Profile
import org.springframework.stereotype.Service

@Service
class ProfileService : IProfileService {

    private val profileRepository: IProfileRepository

    constructor(profileRepository: IProfileRepository) {
        this.profileRepository = profileRepository
    }
    override fun getProfile(authorization: String, userid: String): Profile {
        return profileRepository.getProfileXsi(authorization, userid)
    }
}