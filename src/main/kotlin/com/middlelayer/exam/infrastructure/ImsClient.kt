package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.IClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class ImsClient: IClient {
    private val webClient: WebClient
    private val configuration: WebClientConfiguration = WebClientConfiguration()

    constructor(@Value("ims.server.url") server: String) {
        webClient = WebClient
            .builder()
            .baseUrl(server)
            .filter(ExchangeFilterFunction.ofResponseProcessor {
                configuration.handleApiErrorResponse(it)
            }).build()
    }

    override fun get(uri: String, auth: String?): Mono<String> {
        val response = webClient.get()
            .uri(uri)
            .header("Authorization", auth)
            .retrieve()
            .bodyToMono<String>()
        return response
    }
}