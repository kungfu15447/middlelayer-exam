package com.middlelayer.exam.helpers

import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

class WebTestHelper(private val web: WebTestClient) {
    fun post(uri: String, body: Any): WebTestClient.ResponseSpec {
        return web.post()
            .uri(uri)
            .body(BodyInserters.fromValue(body))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
    }
}