package com.middlelayer.exam.web.dto.settings

import java.util.*

data class PutSettingsStatusDTO(
    val personalAssistance: PersonalAssistantPut?,
    val updateExclusionNumber: ExclusionNumberPut?,
    val exclusionNumberToDelete: String?,
    val newExclusionNumber: String?,
)

data class PersonalAssistantPut(
    val presence: String,
    val enableExpirationTime: Boolean,
    val expirationTime: Date?,
    val enableTransferToAttendant: Boolean,
    val attendantNumber: String,
    val ringSplash: Boolean,
    val callToNumberList: List<CallToNumberPut> = emptyList(),
    val alertMeFirst: Boolean,
    val numberOfRings: Int
)

data class CallToNumberPut(
    val type: String,
    val alternateNumberId: Int?
)

data class ExclusionNumberPut(
    val oldNumber: String,
    val newNumber: String,
)