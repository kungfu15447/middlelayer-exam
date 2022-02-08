package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.Profile

interface IProfileService {
    fun getProfile(authorization: String, userid: String): Profile
}