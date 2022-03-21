package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.domain.*
import com.middlelayer.exam.core.models.ims.PresentationStatusEnum
import com.middlelayer.exam.core.models.xsi.*
import com.middlelayer.exam.core.models.xsi.AssignedCallToNumbers
import com.middlelayer.exam.core.models.xsi.DoNotDisturb
import com.middlelayer.exam.core.models.xsi.ExclusionNumber
import com.middlelayer.exam.core.models.xsi.PersonalAssistant
import com.middlelayer.exam.core.models.xsi.RemoteOffice
import com.middlelayer.exam.core.models.xsi.SimultaneousRingPersonal
import reactor.core.publisher.Mono

interface ISettingsService {
    fun getPersonalAssistant(token: String, userId: String): Mono<DPersonalAssistant>
    fun getPAExclusionNumbers(token: String, userId: String): Mono<List<DExclusionNumber>>
    fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>>
    fun getPAAvailableCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>>
    fun getRemoteOffice(token: String, userId: String): Mono<DRemoteOffice>
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
    fun updatePersonalAssistant(token: String, userId: String, body: PersonalAssistant): Mono<Void>
    fun updatePAAssignedCallToNumbers(token: String, userId: String, body: AssignedCallToNumbers): Mono<Void>
    fun addExclusionNumber(token: String, userId: String, body: ExclusionNumber): Mono<Void>
    fun updateExclusionNumber(token: String, userId: String, oldNumber: String, body: ExclusionNumber): Mono<Void>
    fun deleteExclusionNumber(token: String, userId: String, number: String): Mono<Void>
    fun updateDoNotDisturb(token: String, userId: String, body: DoNotDisturb): Mono<Void>
    fun updateHideNumberStatus(token: String, userId: String, body: NumberDisplayHidden): Mono<Void>
    fun updateNumberPresentationStatus(token: String, userId: String, status: PresentationStatusEnum): Mono<Void>
    fun updateRemoteOffice(token: String, userId: String, body: RemoteOffice): Mono<Void>
    fun updateSimultaneousRingPersonal(token: String, userId: String, body: SimultaneousRingPersonal): Mono<Void>
}