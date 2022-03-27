package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DoNotDisturb(
    val active: Boolean? = null,
    val ringSplash: Boolean? = null,
): XsiModel()