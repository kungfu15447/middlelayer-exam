package com.middlelayer.exam.web

import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class SettingsController {
    private val settingsService: ISettingsService
    private val authService: IAuthService

    @Autowired
    constructor(settingsService: ISettingsService, authService: IAuthService) {
        this.settingsService = settingsService
        this.authService = authService
    }

    @GetMapping("api/user/settings")
    fun getSettings(@RequestHeader("Authorization") token: String): Mono<ResponseEntity<Any>> {
        val userId = ""
        val basicToken = ""

        return this.settingsService.getPersonalAssistant(basicToken, userId).flatMap {
            Mono.just(ResponseEntity(it, HttpStatus.OK))
        }
    }
}