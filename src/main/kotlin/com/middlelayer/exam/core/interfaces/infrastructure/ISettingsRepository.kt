package com.middlelayer.exam.core.interfaces.infrastructure

import com.middlelayer.exam.core.models.xsi.*
import reactor.core.publisher.Mono

interface ISettingsRepository {
    fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant>
    fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>>
    fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<AssignedCallToNumbers>
    fun getPAAvailableCallToNumbers(token: String, userId: String): Mono<AvailableCallToNumbers>
    fun getRemoteOFfice(token: String, userId: String): Mono<RemoteOffice>
}