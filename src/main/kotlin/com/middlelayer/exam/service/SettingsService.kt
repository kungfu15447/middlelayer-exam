package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.infrastructure.ISettingsRepository
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import com.middlelayer.exam.core.models.domain.*
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

    override fun getPersonalAssistant(token: String, userId: String): Mono<DPersonalAssistant> {
        return settingsRepo.getPersonalAssistant(token, userId).flatMap {
            Mono.just(DPersonalAssistant(it))
        }
    }

    override fun getPAExclusionNumbers(token: String, userId: String): Mono<List<DExclusionNumber>> {
        return settingsRepo.getPAExclusionNumbers(token, userId).flatMap {
            Mono.just(it.map { en ->
                DExclusionNumber(en)
            })
        }
    }

    override fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>> {
        return settingsRepo.getPAAssignedCallToNumbers(token, userId).flatMap {
            Mono.just(it.callToNumberList.callToNumber.map { ctn ->
                DCallToNumber(ctn.type?.value ?: "")
            })
        }
    }

    override fun getPAAvailableCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>> {
        return settingsRepo.getPAAvailableCallToNumbers(token, userId).flatMap {
            Mono.just(it.callToNumbers.map { ctn ->
                DCallToNumber(ctn.type?.value ?: "")
            })
        }
    }

    override fun getRemoteOffice(token: String, userId: String): Mono<DRemoteOffice> {
        return settingsRepo.getRemoteOffice(token, userId).flatMap {
            Mono.just(DRemoteOffice(it))
        }
    }

    override fun getNumberDisplayStatus(token: String, userId: String): Mono<DNumberDisplayHidden> {
        return settingsRepo.getNumberDisplayStatus(token, userId).flatMap {
            Mono.just(DNumberDisplayHidden(it))
        }
    }

    override fun getNumberDisplay(token: String, userId: String): Mono<DNumberDisplay> {
        return settingsRepo.getNumberDisplay(token, userId).flatMap {
            Mono.just(DNumberDisplay(it))
        }
    }

    override fun getCallForwardingAlways(token: String, userId: String): Mono<DCallForwardingAlways> {
        return settingsRepo.getCallForwardingAlways(token, userId).flatMap {
            Mono.just(DCallForwardingAlways(it))
        }
    }

    override fun getCallForwardingNoAnswer(token: String, userId: String): Mono<DCallForwardingNoAnswer> {
        return settingsRepo.getCallForwardingNoAnswer(token, userId).flatMap {
            Mono.just(DCallForwardingNoAnswer(it))
        }
    }

    override fun getCallForwardingBusy(token: String, userId: String): Mono<DCallForwardingBusy> {
        return settingsRepo.getCallForwardingBusy(token, userId).flatMap {
            Mono.just(DCallForwardingBusy(it))
        }
    }

    override fun getVoiceMessaging(token: String, userId: String): Mono<DVoiceMessaging> {
        return settingsRepo.getVoiceMessaging(token, userId).flatMap {
            Mono.just(DVoiceMessaging(it))
        }
    }

    override fun getVoiceMessagingGreeting(token: String, userId: String): Mono<DVoiceMessagingGreeting> {
        return settingsRepo.getVoiceMessagingGreeting(token, userId).flatMap {
            Mono.just(DVoiceMessagingGreeting(it))
        }
    }

    override fun getPushNotification(token: String, userId: String): Mono<DPushNotification> {
        return settingsRepo.getMWIDeliveryToMobileEndpoint(token, userId).flatMap {
            Mono.just(DPushNotification(it))
        }
    }

    override fun getSimultaneousRing(token: String, userId: String): Mono<DSimultaneousRing> {
        return settingsRepo.getSimultaneousRingPersonal(token, userId).flatMap {
            Mono.just(DSimultaneousRing(it))
        }
    }

    override fun getDoNotDisturb(token: String, userId: String): Mono<DDoNotDisturb> {
        return settingsRepo.getDoNotDisturb(token, userId).flatMap {
            Mono.just(DDoNotDisturb(it))
        }
    }

    override fun updatePersonalAssistant(token: String, userId: String, body: PersonalAssistant): Mono<Void> {
        return settingsRepo.updatePersonalAssistant(token, userId, body)
    }

    override fun updatePAAssignedCallToNumbers(token: String, userId: String, body: AssignedCallToNumbers): Mono<Void> {
        return settingsRepo.updatePAAssignedCallToNumbers(token, userId, body);
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
}