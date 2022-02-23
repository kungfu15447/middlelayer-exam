package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.CallForwardingNoAnswer

data class DCallForwardingNoAnswer(
    var active: Boolean,
    var phoneNumber: String?,
    var numberOfRings: Int
) {
    constructor(xsiCallForwardingNoAnswer: CallForwardingNoAnswer) : this(
        active = xsiCallForwardingNoAnswer.active ?: false,
        phoneNumber = xsiCallForwardingNoAnswer.forwardToPhoneNumber,
        numberOfRings = xsiCallForwardingNoAnswer.numberOfRings ?: 0
    )
}