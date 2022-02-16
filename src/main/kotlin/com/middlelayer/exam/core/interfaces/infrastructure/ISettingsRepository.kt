package com.middlelayer.exam.core.interfaces.infrastructure

import com.middlelayer.exam.core.models.xsi.ExclusionNumber
import com.middlelayer.exam.core.models.xsi.PersonalAssistant
import com.middlelayer.exam.core.models.xsi.PersonalAssistantAssignedCallToNumbers
import reactor.core.publisher.Mono

interface ISettingsRepository {
    fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant>
    fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>>
    fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<PersonalAssistantAssignedCallToNumbers>
}