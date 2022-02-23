package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.CallForwardingAlways

data class DCallForwardingAlways(
    var active: Boolean,
    var phoneNumber: String?
) {
    constructor(xsiCallForwardingAlways: CallForwardingAlways) : this(
        active = xsiCallForwardingAlways.active ?: false,
        phoneNumber = xsiCallForwardingAlways.forwardToPhoneNumber
    )
}