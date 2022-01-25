package com.middlelayer.exam.service

import org.springframework.boot.autoconfigure.integration.IntegrationProperties
import org.springframework.stereotype.Service

@Service
class ClientService {
    lateinit var webClient: IntegrationProperties.RSocket.Client
}