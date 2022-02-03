package com.middlelayer.exam.core.interfaces.infrastructure

interface IXsiClient {
    fun get(uri: String, auth: String? = null): String
}