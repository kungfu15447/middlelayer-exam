package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.Profile

data class DProfile(val userId: String,
                    val groupId: String,
                    val firstName: String,
                    val lastName: String,
                    val mobile: String,
                    val number: String,
                    val email: String) {

    constructor(xsiProfile: Profile) : this(
        userId = xsiProfile.details.userId ?: "",
        groupId = xsiProfile.details.groupId,
        firstName = xsiProfile.details.firstName?: "",
        lastName = xsiProfile.details.lastName?: "",
        mobile = xsiProfile.additionalDetails.mobile?: "",
        email = xsiProfile.additionalDetails.emailAddress?: "",
        number = xsiProfile.details.number?: ""
    )
}