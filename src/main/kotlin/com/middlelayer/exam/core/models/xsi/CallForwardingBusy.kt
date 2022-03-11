package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CallForwardingBusy(
    val active: Boolean? = null,
    val forwardToPhoneNumber: String? = null,
): XsiModel()