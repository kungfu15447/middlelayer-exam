package com.middlelayer.exam.web.socket

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.middlelayer.exam.core.models.xsi.Contact
import org.springframework.http.HttpRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import reactor.core.publisher.Mono
import java.util.concurrent.CopyOnWriteArraySet


@Component
class ContactHandler : WebSocketHandler
{

    private val sessions: MutableSet<WebSocketSession> = CopyOnWriteArraySet()

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        emit("Message Received")
        for(s in sessions) {
            println(s.id)
            emit(s.id)
        }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
    }

    fun emit(contact: Mono<ResponseEntity<Any>>) {
        for(socket in sessions) {
            socket.sendMessage(TextMessage(jacksonObjectMapper().writeValueAsString(contact)))
        }
    }

    fun emit(contact: Contact) {
        for(socket in sessions) {
            socket.sendMessage(TextMessage(jacksonObjectMapper().writeValueAsString(contact)))
        }
    }

    fun emit(msg: String) {
        for(socket in sessions) {
            socket.sendMessage(TextMessage(msg))
            print(socket.id)
        }
    }

    @Throws(Exception::class)
    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
        println("connection established")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("connection closed")
    }

    override fun supportsPartialMessages(): Boolean {
        return false
    }

}