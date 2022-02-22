package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.domain.*
import com.middlelayer.exam.core.models.ims.NumberDisplay
import com.middlelayer.exam.core.models.xsi.*
import reactor.core.publisher.Mono

interface ISettingsService {
    fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant>
    fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>>
    fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>>
    fun getPAAvailableCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>>
    fun getRemoteOffice(token: String, userId: String): Mono<RemoteOffice>
    fun getNumberDisplayStatus(token: String, userId: String): Mono<DNumberDisplayHidden>
    fun getNumberDisplay(token: String, userId: String): Mono<DNumberDisplay>
    fun getCallForwardingAlways(token: String, userId: String): Mono<DCallForwardingAlways>
    fun getCallForwardingNoAnswer(token: String, userId: String): Mono<DCallForwardingNoAnswer>
    fun getCallForwardingBusy(token: String, userId: String): Mono<DCallForwardingBusy>
    fun getVoiceMessaging(token: String, userId: String): Mono<DVoiceMessaging>
    fun getVoiceMessagingGreeting(token: String, userId: String): Mono<DVoiceMessagingGreeting>
    fun getPushNotification(token: String, userId: String): Mono<DPushNotification>
    fun getSimultaneousRing(token: String, userId: String): Mono<DSimultaneousRing>
    fun getDoNotDisturb(token: String, userId: String): Mono<DDoNotDisturb>
}