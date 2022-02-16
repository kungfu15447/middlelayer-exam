package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonProperty

data class CallToNumberList(
    @JsonProperty("callToNumber")
    val callToNumbers: List<CallToNumber> = emptyList()
)

data class CallToNumber(
    val type: String? = null
)