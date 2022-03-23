package com.middlelayer.exam.web.socket

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.socket.client.StandardWebSocketClient
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.client.WebSocketConnectionManager
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(ContactHandler(), "/frederik").setAllowedOrigins("*")
    }
}