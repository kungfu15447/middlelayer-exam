package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.xsi.PersonalAssistant
import reactor.core.publisher.Mono

interface ISettingsService {
    fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant>
}