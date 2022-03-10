package com.middlelayer.exam.core.interfaces.infrastructure

import com.middlelayer.exam.core.models.ims.NumberDisplay
import com.middlelayer.exam.core.models.xsi.*
import reactor.core.publisher.Mono

interface ISettingsRepository {
    fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant>
    fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>>
    fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<AssignedCallToNumbers>
    fun getPAAvailableCallToNumbers(token: String, userId: String): Mono<AvailableCallToNumbers>
    fun getRemoteOffice(token: String, userId: String): Mono<RemoteOffice>
    fun getNumberDisplayStatus(token: String, userId: String): Mono<NumberDisplayHidden>
    fun getNumberDisplay(token: String, userId: String): Mono<NumberDisplay>
    fun getCallForwardingAlways(token: String, userId: String): Mono<CallForwardingAlways>
    fun getCallForwardingNoAnswer(token: String, userId: String): Mono<CallForwardingNoAnswer>
    fun getCallForwardingBusy(token: String, userId: String): Mono<CallForwardingBusy>
    fun getVoiceMessaging(token: String, userId: String): Mono<VoiceMessaging>
    fun getVoiceMessagingGreeting(token: String, userId: String): Mono<VoiceMessagingGreeting>
    fun getMWIDeliveryToMobileEndpoint(token: String, userId: String): Mono<MWIDeliveryToMobileEndpoint>
    fun getSimultaneousRingPersonal(token: String, userId: String): Mono<SimultaneousRingPersonal>
    fun getDoNotDisturb(token: String, userId: String): Mono<DoNotDisturb>
    fun updatePersonalAssistant(token: String, userId: String, body: PersonalAssistant): Mono<Void>
    fun updatePAAssignedCallToNumbers(token: String, userId: String, body: AssignedCallToNumbers): Mono<Void>
    fun addExclusionNumber(token: String, userId: String, body: ExclusionNumber): Mono<Void>
    fun updateExclusionNumber(token: String, userId: String, number: String, body: ExclusionNumber): Mono<Void>
    fun deleteExclusionNumber(token: String, userId: String, number: String): Mono<Void>
    fun updateNumberDisplayStatus(token: String, userId: String, body: NumberDisplayHidden): Mono<Void>
}