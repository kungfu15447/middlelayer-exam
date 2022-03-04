package com.middlelayer.exam.web.dto.profile

import com.middlelayer.exam.core.models.domain.DProfile
import com.middlelayer.exam.core.models.domain.DService

data class LoginDTOResponse(
    var token: String,
    var profile: DProfile,
    var services: List<DService>
)