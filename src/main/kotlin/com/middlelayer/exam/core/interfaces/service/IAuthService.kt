package com.middlelayer.exam.core.interfaces.service

interface IAuthService {
    fun register(authorization: String, userid: String): String
}