package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class PersonalAssistant(
    val presence: String? = null,
    val enableExpirationTime: Boolean? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val expirationTime: Date? = null,
    val enableTransferToAttendant: Boolean? = null,
    val attendantNumber: String? = null,
    val ringSplash: String? = null,
    val callToNumberList: CallToNumberList = CallToNumberList(),
    val alertMeFirst: Boolean? = null,
    val numberOfRings: Int? = null
)

data class CallToNumberList(
    @JsonProperty("callToNumber")
    val callToNumbers: List<CallToNumber> = emptyList()
)

data class CallToNumber(
    val type: String? = null
)