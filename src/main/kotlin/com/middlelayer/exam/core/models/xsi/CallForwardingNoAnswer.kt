package com.middlelayer.exam.core.models.xsi

data class CallForwardingNoAnswer(
    val active: Boolean? = null,
    val forwardToPhoneNumber: String? = null,
    val numberOfRings: Int? = null,
)