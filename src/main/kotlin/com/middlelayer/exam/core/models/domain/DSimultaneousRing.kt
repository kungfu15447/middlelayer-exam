package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.CriteriaActivation
import com.middlelayer.exam.core.models.xsi.SimultaneousRingPersonal

data class DSimultaneousRing(
    var active: Boolean,
    var doNotRingIfOnCall: Boolean,
    var phoneNumbers: List<DPhoneNumber>
) {
    constructor(xsiSimultaneousRingPersonal: SimultaneousRingPersonal) : this(
        active = xsiSimultaneousRingPersonal.active ?: false,
        doNotRingIfOnCall = false,
        phoneNumbers = emptyList()
    ) {
        var incomingCalls = xsiSimultaneousRingPersonal.incomingCalls ?: ""
        var phoneNumberList = xsiSimultaneousRingPersonal.criteriaActivationList.criteriaActivations

        doNotRingIfOnCall = incomingCalls.lowercase() != "do not ring if on a call"
        if (phoneNumberList.isNotEmpty()) {
            phoneNumbers = phoneNumberList.map {
                DPhoneNumber(it)
            }
        }

    }

}

data class DPhoneNumber(
    var address: String,
    var answerConfirmationRequired: Boolean,
) {
    constructor(xsiPhoneNumber: CriteriaActivation) : this(
        address = xsiPhoneNumber.address ?: "",
        answerConfirmationRequired = xsiPhoneNumber.answerConfirmationRequired ?: false
    )
}