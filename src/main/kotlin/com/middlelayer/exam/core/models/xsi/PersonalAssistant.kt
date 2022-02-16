package com.middlelayer.exam.core.models.xsi

data class PersonalAssistant(
    val presence: String?,
    val enableExpirationTime: Boolean,
    val enableTransferToAttendant: Boolean,
    val attendantNumber: String?,
    val callToNumberList: List<CallToNumber>,
    val alertMeFirst: Boolean,
    val numberOfRings: Int
)

data class CallToNumber(
    val type: String?
)