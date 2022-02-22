package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class PersonalAssistant(
    val presence: String? = null,
    val enableExpirationTime: Boolean? = null,
    val expirationTime: Date? = null,
    val enableTransferToAttendant: Boolean? = null,
    val attendantNumber: String? = null,
    val ringSplash: Boolean? = null,
    val callToNumberList: CallToNumberList = CallToNumberList(),
    val alertMeFirst: Boolean? = null,
    val numberOfRings: Int? = null
)