package com.middlelayer.exam.core.interfaces.infrastructure

import reactor.core.publisher.Mono

interface IClient {
    fun get(uri: String, auth: String? = null): Mono<String>
    fun post(uri: String, auth: String? = null, body: String? = null): Mono<String>
    fun put(uri: String, auth: String? = null, body: String? = null): Mono<String>
    fun delete(uri: String, auth: String? = null, body: String? = null): Mono<String>
}