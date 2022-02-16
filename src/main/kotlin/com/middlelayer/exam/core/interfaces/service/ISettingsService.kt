package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.domain.DCallToNumber
import com.middlelayer.exam.core.models.xsi.ExclusionNumber
import com.middlelayer.exam.core.models.xsi.PersonalAssistant
import reactor.core.publisher.Mono

interface ISettingsService {
    fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant>
    fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>>
    fun getPaAssignedCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>>
}