package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CallToNumber(
    val type: String? = null,
    val alternateNumberId: Int? = null,
)