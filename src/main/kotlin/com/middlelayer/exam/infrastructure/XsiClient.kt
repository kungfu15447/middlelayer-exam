package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.IClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono

@Component
@Qualifier("xsiClient")
class XsiClient : IClient {
    private val webClient: WebClient
    private val configuration: WebClientConfiguration = WebClientConfiguration()

    constructor(@Value("\${xsi.server.url}") server: String) {
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

    override fun post(uri: String, auth: String?, body: String?): Mono<String> {
        TODO("Not yet implemented")
    }

    override fun put(uri: String, auth: String?, body: String?): Mono<String> {
        val response = webClient.put()
            .uri(uri)
            .header("Authorization", auth)
        body?.let {
            response.body(Mono.just(it))
        }
        return response
            .retrieve()
            .bodyToMono()
    }

    override fun delete(uri: String, auth: String?, body: String?): Mono<String> {
        TODO("Not yet implemented")
    }
}