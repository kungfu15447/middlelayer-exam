package com.middlelayer.exam.helpers
import org.springframework.test.web.reactive.server.WebTestClient

class WebTestHelper(private val web: WebTestClient) {
    fun post(uri: String, headers: List<WebHeader>? = null, body: Any? = null): WebTestClient.ResponseSpec {
        var request = web.post()
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

    fun delete(uri: String, headers: List<WebHeader>? = null): WebTestClient.ResponseSpec {
        var request = web.delete()
                .uri(uri)
        headers?.let {
            it.forEach { header ->
                request.header(header.key, header.value)
            }
        }

        return request.exchange()
    }
}