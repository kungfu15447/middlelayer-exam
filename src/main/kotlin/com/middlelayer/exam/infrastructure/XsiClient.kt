package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.IXsiClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class XsiClient : IXsiClient {

    //TODO Look up how to implement WebClient in Spring Boot. RestTemplate will sooner or later be deprecated
    @Value("\${server.url}")
    private val server: String = ""

    constructor() {
    }

    override fun get(uri: String, auth: String?): String {
        return ""
    }
}