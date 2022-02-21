package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.IClient
import reactor.core.publisher.Mono

class IMSClient: IClient {
    override fun get(uri: String, auth: String?): Mono<String> {
        TODO("Not yet implemented")
    }
}