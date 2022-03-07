package com.middlelayer.exam.web.dto.settings

import java.util.*

data class PutSettingsStatusDTO(
    val personalAssistance: PersonalAssistantPut
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
)