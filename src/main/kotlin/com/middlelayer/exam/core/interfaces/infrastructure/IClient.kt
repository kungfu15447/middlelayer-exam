package com.middlelayer.exam.core.interfaces.infrastructure

import reactor.core.publisher.Mono

interface IClient {
    fun get(uri: String, auth: String? = null): Mono<String>
}