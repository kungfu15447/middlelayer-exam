package com.middlelayer.exam.web.dto.settings

data class PutDoNotDisturbDTO(
    val active: Boolean,
    val ringSplash: Boolean
)