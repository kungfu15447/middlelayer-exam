package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.domain.DCallToNumber
import com.middlelayer.exam.core.models.ims.NumberDisplay
import com.middlelayer.exam.core.models.xsi.*
import reactor.core.publisher.Mono

interface ISettingsService {
    fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant>
    fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>>
    fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>>
    fun getPAAvailableCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>>
    fun getRemoteOffice(token: String, userId: String): Mono<RemoteOffice>
    fun getNumberDisplayStatus(token: String, userId: String): Mono<NumberDisplayHidden>
    fun getNumberDisplay(token: String, userId: String): Mono<NumberDisplay>
    fun getCallForwardingAlways(token: String, userId: String): Mono<CallForwardingAlways>
    fun getCallForwardingNoAnswer(token: String, userId: String): Mono<CallForwardingNoAnswer>
    fun getCallForwardingBusy(token: String, userId: String): Mono<CallForwardingBusy>
    fun getVoiceMessaging(token: String, userId: String): Mono<VoiceMessaging>
}