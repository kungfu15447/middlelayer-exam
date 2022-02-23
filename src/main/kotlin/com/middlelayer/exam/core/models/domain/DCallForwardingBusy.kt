package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.CallForwardingBusy

data class DCallForwardingBusy(
    var active: Boolean,
    var phoneNumber: String?
) {
    constructor(xsiCallForwardingBusy: CallForwardingBusy) : this(
        active = xsiCallForwardingBusy.active ?: false,
        phoneNumber = xsiCallForwardingBusy.forwardToPhoneNumber
    )
}