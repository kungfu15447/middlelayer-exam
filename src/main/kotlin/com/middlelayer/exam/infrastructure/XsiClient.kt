package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.IXsiClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitExchange
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class XsiClient : IXsiClient {

    //TODO Look up how to implement WebClient in Spring Boot. RestTemplate will sooner or later be deprecated
    @Value("\${server.url}")
    private val server: String = ""

    private val webClient: WebClient

    constructor() {
        print(server)
        webClient = WebClient
                        .builder()
                        .baseUrl("https://scalexi-pp-xsp2.tdc.dk")
                        .build()
    }

    override fun get(uri: String, auth: String?): String {
        val response = webClient.get()
            .uri(uri)
            .header("Authorization", auth)
            .retrieve()
            .bodyToMono<String>().block()
        return response!!
    }
}