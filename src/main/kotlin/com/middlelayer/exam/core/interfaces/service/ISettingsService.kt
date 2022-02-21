package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.domain.DCallToNumber
import com.middlelayer.exam.core.models.xsi.ExclusionNumber
import com.middlelayer.exam.core.models.xsi.PersonalAssistant
import com.middlelayer.exam.core.models.xsi.RemoteOffice
import reactor.core.publisher.Mono

interface ISettingsService {
    fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant>
    fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>>
    fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>>
    fun getPAAvailableCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>>
    fun getRemoteOffice(token: String, userId: String): Mono<RemoteOffice>
}