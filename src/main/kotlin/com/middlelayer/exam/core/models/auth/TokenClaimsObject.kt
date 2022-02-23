package com.middlelayer.exam.core.models.auth

data class TokenClaimsObject(
    val services: List<String>,
    val profileObj: ProfileTokenObject,
    val basicToken: String
)