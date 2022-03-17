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

    fun get(uri: String, headers: List<WebHeader>? = null): WebTestClient.ResponseSpec {
        var request = web.get()
            .uri(uri)
        headers?.let {
            it.forEach { header ->
                request.header(header.key, header.value)
            }
        }
        return request.exchange()
    }

    fun put(uri: String, headers: List<WebHeader>? = null, body: Any? = null): WebTestClient.ResponseSpec {
        var request = web.put()
            .uri(uri)
        headers?.let {
            it.forEach { header ->
                request.header(header.key, header.value)
            }
        }
        body?.let {
            request
                .bodyValue(body)
        }
        return request.exchange()
    }
}