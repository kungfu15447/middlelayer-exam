package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.exceptions.BadRequestException
import com.middlelayer.exam.core.exceptions.NotFoundException
import com.middlelayer.exam.core.exceptions.UnauthorizedException
import com.middlelayer.exam.core.interfaces.infrastructure.IXsiClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono

@Component
class XsiClient : IXsiClient {

    private val webClient: WebClient

    constructor(@Value("\${server.url}") server: String) {
        print(server)
        webClient = WebClient
                        .builder()
                        .baseUrl(server)
                        .filter(ExchangeFilterFunction.ofResponseProcessor(this::handleApiErrorResponse))
                        .build()
    }

    private fun handleApiErrorResponse(clientResponse: ClientResponse): Mono<ClientResponse> {
        println("Something something")
        var statusCode = clientResponse.statusCode()
        when(statusCode) {
            HttpStatus.NOT_FOUND -> throw NotFoundException("Route could not be found")
            HttpStatus.UNAUTHORIZED -> throw UnauthorizedException("Tried to access unauthorized route")
        }
        return Mono.just(clientResponse)
    }

    override fun get(uri: String, auth: String?): String? {
        val response = webClient.get()
            .uri(uri)
            .header("Authorization", auth)
            .retrieve()
            .bodyToMono<String>()
            .block()
        return response
    }
}