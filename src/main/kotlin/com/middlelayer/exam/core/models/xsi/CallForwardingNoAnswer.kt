package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CallForwardingNoAnswer(
    val active: Boolean? = null,
    val forwardToPhoneNumber: String? = null,
    val numberOfRings: Int? = null,
): XsiModel()