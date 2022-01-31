package com.middlelayer.exam.core.interfaces.infrastructure
import com.middlelayer.exam.core.models.Profile

interface IProfileRepository {
    fun getProfileXsi(authorization: String, userid: String): Profile
}