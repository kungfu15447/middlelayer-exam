package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.exceptions.BadRequestException
import com.middlelayer.exam.core.exceptions.ISEException
import com.middlelayer.exam.core.exceptions.NotFoundException
import com.middlelayer.exam.core.exceptions.UnauthorizedException
import com.middlelayer.exam.core.interfaces.infrastructure.IXsiClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
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
        var statusCode = clientResponse.statusCode()
        println(statusCode)
        if (statusCode.isError) {
            println("This was an error")
            return clientResponse.bodyToMono(String::class.java).flatMap<ClientResponse?> {
                when(statusCode) {
                    HttpStatus.NOT_FOUND -> Mono.error(NotFoundException(it))
                    HttpStatus.UNAUTHORIZED -> Mono.error(UnauthorizedException(it))
                    HttpStatus.INTERNAL_SERVER_ERROR -> Mono.error(ISEException(it))
                    else -> {
                        Mono.error(BadRequestException(it))
                    }
                }
            }.switchIfEmpty(Mono.error(UnauthorizedException("Unauthorized access")))
        }
        return Mono.just(clientResponse)
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