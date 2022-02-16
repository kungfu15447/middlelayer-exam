package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*

data class PersonalAssistant(
    val presence: String? = null,
    val enableExpirationTime: Boolean? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val expirationTime: Date? = null,
    val enableTransferToAttendant: Boolean? = null,
    val attendantNumber: String? = null,
    val ringSplash: String? = null,
    val alertMeFirst: Boolean? = null,
    val numberOfRings: Int? = null
)