package com.middlelayer.exam.core.models.auth

data class ProfileTokenObject(
    val userId: String  = "",
    val groupId: String = "",
    val fullName: String = "",
    val email: String = ""
)