package com.middlelayer.exam.web.dto.profile

import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service

data class LoginDTOResponse(
    var token: String,
    var profile: LoginProfile,
    var services: List<String>
) {
    constructor(
            token: String,
            xsiProfile: Profile,
            xsiServices: List<Service>
    ) : this(
            token = token,
            profile = LoginProfile(xsiProfile),
            services = xsiServices.mapNotNull {
                it.name
            }
    )
}

data class LoginProfile(
        val userId: String,
        val groupId: String,
        val firstName: String,
        val lastName: String,
        val mobile: String,
        val number: String,
        val email: String
) {
    constructor(xsiProfile: Profile) : this(
            userId = xsiProfile.details.userId ?: "",
            groupId = xsiProfile.details.groupId ?: "",
            firstName = xsiProfile.details.firstName?: "",
            lastName = xsiProfile.details.lastName?: "",
            mobile = xsiProfile.additionalDetails.mobile?: "",
            email = xsiProfile.additionalDetails.emailAddress?: "",
            number = xsiProfile.details.number?: ""
    )
}

