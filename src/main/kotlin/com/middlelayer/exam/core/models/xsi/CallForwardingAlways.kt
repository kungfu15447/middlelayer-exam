package com.middlelayer.exam.core.models.xsi

data class CallForwardingAlways(
    val active: Boolean? = null,
    val forwardToPhoneNumber: String? = null,
    val ringSplash: Boolean? = null
): XsiModel()