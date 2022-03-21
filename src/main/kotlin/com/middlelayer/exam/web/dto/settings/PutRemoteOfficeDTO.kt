package com.middlelayer.exam.web.dto.settings

data class PutRemoteOfficeDTO(
    val active: Boolean,
    val remoteOfficeNumber: String,
)