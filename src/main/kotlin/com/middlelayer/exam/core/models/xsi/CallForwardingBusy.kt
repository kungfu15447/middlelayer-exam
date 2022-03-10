package com.middlelayer.exam.core.models.xsi

data class CallForwardingBusy(
    val active: Boolean? = null,
    val forwardToPhoneNumber: String? = null,
): XsiModel()