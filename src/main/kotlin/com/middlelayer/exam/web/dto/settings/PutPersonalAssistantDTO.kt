package com.middlelayer.exam.web.dto.settings

import java.util.*

data class PutPersonalAssistantDTO(
    val presence: String,
    val expirationTime: Date?,
    val transferNumber: String?,
    val transferCalls: Boolean,
    val transferNotification: Boolean,
    val assignedCallToNumbers: List<String>?,
)