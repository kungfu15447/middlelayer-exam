package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.infrastructure.ISettingsRepository
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import com.middlelayer.exam.core.models.domain.*
import com.middlelayer.exam.core.models.ims.NumberDisplay
import com.middlelayer.exam.core.models.ims.PresentationStatusEnum
import com.middlelayer.exam.core.models.xsi.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SettingsService : ISettingsService {
    private val settingsRepo: ISettingsRepository

    @Autowired
    constructor(settingsRepo: ISettingsRepository) {
        this.settingsRepo = settingsRepo
    }

    override fun getSettings(token: String, userId: String): Mono<UserSettings> {
        val personalAssistant = settingsRepo.getPersonalAssistant(token, userId)
        val exclusionNumbers = settingsRepo.getPAExclusionNumbers(token, userId)
        val assignedCallToNumbers = settingsRepo.getPAAssignedCallToNumbers(token, userId)
        val availableCallToNumbers = settingsRepo.getPAAvailableCallToNumbers(token, userId)
        val remoteOffice = settingsRepo.getRemoteOffice(token, userId)
        val numberDisplayStatus = settingsRepo.getNumberDisplayStatus(token, userId)
        val numberDisplay = settingsRepo.getNumberDisplay(token, userId)
        val callForwardingAlways = settingsRepo.getCallForwardingAlways(token, userId)
        val callForwardingBusy = settingsRepo.getCallForwardingBusy(token, userId)
        val callForwardingNoAnswer = settingsRepo.getCallForwardingNoAnswer(token, userId)
        val voiceMessaging = settingsRepo.getVoiceMessaging(token, userId)
        val voiceMessagingGreeting = settingsRepo.getVoiceMessagingGreeting(token, userId)
        val pushNotification = settingsRepo.getMWIDeliveryToMobileEndpoint(token, userId)
        val simultaneousRing = settingsRepo.getSimultaneousRingPersonal(token, userId)
        val doNotDisturb = settingsRepo.getDoNotDisturb(token, userId)

        val personalAssistantZip = Mono.zip(
                personalAssistant,
                exclusionNumbers,
                assignedCallToNumbers,
                availableCallToNumbers
        )

        val numberDisplayZip = Mono.zip(
                numberDisplayStatus,
                numberDisplay
        )

        val callForwardZip = Mono.zip(
                callForwardingAlways,
                callForwardingBusy,
                callForwardingNoAnswer
        )

        val voiceMessagingZip = Mono.zip(
                voiceMessaging,
                voiceMessagingGreeting,
                pushNotification,
        )

        val settings = Mono.zip(
                personalAssistantZip,
                remoteOffice,
                numberDisplayZip,
                callForwardZip,
                voiceMessagingZip,
                simultaneousRing,
                doNotDisturb
        )
        return settings.flatMap {
            val paZip = it.t1
            val ndZip = it.t3
            val cfZip = it.t4
            val vmZip = it.t5

            //Personal Assistant settings
            val pa: PersonalAssistant = paZip.t1
            val en: List<ExclusionNumber> = paZip.t2
            val asctn: AssignedCallToNumbers = paZip.t3
            val avctn: AvailableCallToNumbers = paZip.t4

            //Number Display settings
            val ndh: NumberDisplayHidden = ndZip.t1
            val nd: NumberDisplay = ndZip.t2

            //Remote Office settings
            val ro: RemoteOffice = it.t2

            //Call Forwarding settings
            val cfa: CallForwardingAlways = cfZip.t1
            val cfb: CallForwardingBusy = cfZip.t2
            val cfna: CallForwardingNoAnswer = cfZip.t3

            //Voice Messaging settings
            val vm: VoiceMessaging = vmZip.t1
            val vmg: VoiceMessagingGreeting = vmZip.t2
            val pn: MWIDeliveryToMobileEndpoint = vmZip.t3

            //Simultaneous Ring settings
            val sr: SimultaneousRingPersonal = it.t6

            //Do Not Disturbs settings
            val dnd: DoNotDisturb = it.t7

            val userSettings = UserSettings(
                    pa,
                    asctn.callToNumberList.callToNumber,
                    avctn.callToNumbers,
                    en,
                    ro,
                    cfa,
                    cfb,
                    cfna,
                    ndh,
                    nd,
                    vm,
                    vmg,
                    pn,
                    sr,
                    dnd
            )

            Mono.just(userSettings)
        }
    }

    override fun updatePersonalAssistant(token: String, userId: String, body: PersonalAssistant): Mono<Void> {
        return settingsRepo.updatePersonalAssistant(token, userId, body)
    }

    override fun updatePAAssignedCallToNumbers(token: String, userId: String, body: AssignedCallToNumbers): Mono<Void> {
        return settingsRepo.updatePAAssignedCallToNumbers(token, userId, body)
    }

    override fun addExclusionNumber(token: String, userId: String, body: ExclusionNumber): Mono<Void> {
        return settingsRepo.addExclusionNumber(token, userId, body)
    }

    override fun updateExclusionNumber(
        token: String,
        userId: String,
        oldNumber: String,
        body: ExclusionNumber
    ): Mono<Void> {
        return settingsRepo.updateExclusionNumber(token, userId, oldNumber, body)
    }

    override fun deleteExclusionNumber(token: String, userId: String, number: String): Mono<Void> {
        return settingsRepo.deleteExclusionNumber(token, userId, number)
    }
    override fun updateRemoteOffice(token: String, userId: String, body: RemoteOffice): Mono<Void> {
        return settingsRepo.updateRemoteOffice(token, userId, body)
    }

    override fun updateHideNumberStatus(token: String, userId: String, body: NumberDisplayHidden): Mono<Void> {
        return settingsRepo.updateNumberDisplayStatus(token, userId, body)
    }

    override fun updateNumberPresentationStatus(token: String, userId: String, status: PresentationStatusEnum): Mono<Void> {
        return when (status) {
            PresentationStatusEnum.BUSINESS -> settingsRepo.updatePresentationToBusiness(token, userId)
            PresentationStatusEnum.MOBILE -> settingsRepo.updatePresentationToMobile(token, userId)
        }
    }

    override fun updateDoNotDisturb(token: String, userId: String, body: DoNotDisturb): Mono<Void> {
        return settingsRepo.updateDoNotDisturb(token, userId, body)
    }

    override fun updateCallForwardingAlways(token: String, userId: String, body: CallForwardingAlways): Mono<Void> {
        return settingsRepo.updateCallForwardingAlways(token, userId, body)
    }

    override fun updateCallForwardingBusy(token: String, userId: String, body: CallForwardingBusy): Mono<Void> {
        return settingsRepo.updateCallForwardingBusy(token, userId, body)
    }

    override fun updateCallForwardingNoAnswer(token: String, userId: String, body: CallForwardingNoAnswer): Mono<Void> {
        return settingsRepo.updateCallForwardingNoAnswer(token, userId, body)
    }

    override fun updateSimultaneousRingPersonal(
        token: String,
        userId: String,
        body: SimultaneousRingPersonal
    ): Mono<Void> {
        return settingsRepo.updateSimultaneousRingPersonal(token, userId, body)
    }
}