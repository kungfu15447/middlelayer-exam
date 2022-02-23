package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.PersonalAssistant
import java.util.*

data class DPersonalAssistant(
    var presence: String,
    var enableExpirationTime: Boolean,
    var expirationTime: Date?,
    var enableTransferToAttendant: Boolean,
    var attendantNumber: String?,
    var ringSplash: Boolean,
    var callToNumbers: List<DCallToNumber>
) {
    constructor(xsiPersonalAssistant: PersonalAssistant) : this(
        presence = xsiPersonalAssistant.presence ?: "",
        enableExpirationTime = xsiPersonalAssistant.enableExpirationTime ?: false,
        expirationTime = xsiPersonalAssistant.expirationTime,
        enableTransferToAttendant = xsiPersonalAssistant.enableTransferToAttendant ?: false,
        attendantNumber = xsiPersonalAssistant.attendantNumber,
        ringSplash = xsiPersonalAssistant.ringSplash ?: false,
        callToNumbers = emptyList()
    ) {
        val xsiCallToNumbers = xsiPersonalAssistant.callToNumberList.callToNumbers
        callToNumbers = xsiCallToNumbers.map {
            DCallToNumber(it.type ?: "")
        }
    }
}