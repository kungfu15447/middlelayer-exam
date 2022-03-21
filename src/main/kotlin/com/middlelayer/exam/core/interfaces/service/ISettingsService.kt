package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.domain.*
import com.middlelayer.exam.core.models.ims.PresentationStatusEnum
import com.middlelayer.exam.core.models.xsi.*
import reactor.core.publisher.Mono

interface ISettingsService {
    fun getSettings(token: String, userId: String): Mono<UserSettings>
    fun updatePersonalAssistant(token: String, userId: String, body: PersonalAssistant): Mono<Void>
    fun updatePAAssignedCallToNumbers(token: String, userId: String, body: AssignedCallToNumbers): Mono<Void>
    fun addExclusionNumber(token: String, userId: String, body: ExclusionNumber): Mono<Void>
    fun updateExclusionNumber(token: String, userId: String, oldNumber: String, body: ExclusionNumber): Mono<Void>
    fun deleteExclusionNumber(token: String, userId: String, number: String): Mono<Void>
    fun updateDoNotDisturb(token: String, userId: String, body: DoNotDisturb): Mono<Void>
    fun updateCallForwardingAlways(token: String, userId: String, body: CallForwardingAlways): Mono<Void>
    fun updateCallForwardingBusy(token: String, userId: String, body: CallForwardingBusy): Mono<Void>
    fun updateCallForwardingNoAnswer(token: String, userId: String, body: CallForwardingNoAnswer): Mono<Void>
    fun updateHideNumberStatus(token: String, userId: String, body: NumberDisplayHidden): Mono<Void>
    fun updateNumberPresentationStatus(token: String, userId: String, status: PresentationStatusEnum): Mono<Void>
    fun updateRemoteOffice(token: String, userId: String, body: RemoteOffice): Mono<Void>
    fun updateSimultaneousRingPersonal(token: String, userId: String, body: SimultaneousRingPersonal): Mono<Void>
}