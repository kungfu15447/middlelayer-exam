package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.infrastructure.ISettingsRepository
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import com.middlelayer.exam.core.models.domain.DCallToNumber
import com.middlelayer.exam.core.models.ims.NumberDisplay
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

    override fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant> {
        return settingsRepo.getPersonalAssistant(token, userId)
    }

    override fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>> {
        return settingsRepo.getPAExclusionNumbers(token, userId)
    }

    override fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>> {
        return settingsRepo.getPAAssignedCallToNumbers(token, userId).flatMap {
            Mono.just(it.callToNumberList.callToNumbers.map { ctn ->
                DCallToNumber(ctn.type ?: "")
            })
        }
    }

    override fun getPAAvailableCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>> {
        return settingsRepo.getPAAvailableCallToNumbers(token, userId).flatMap {
            Mono.just(it.callToNumbers.map { ctn ->
                DCallToNumber(ctn.type ?: "")
            })
        }
    }

    override fun getRemoteOffice(token: String, userId: String): Mono<RemoteOffice> {
        return settingsRepo.getRemoteOffice(token, userId)
    }

    override fun getNumberDisplayStatus(token: String, userId: String): Mono<NumberDisplayHidden> {
        return settingsRepo.getNumberDisplayStatus(token, userId)
    }

    override fun getNumberDisplay(token: String, userId: String): Mono<NumberDisplay> {
        return settingsRepo.getNumberDisplay(token, userId)
    }

    override fun getCallForwardingAlways(token: String, userId: String): Mono<CallForwardingAlways> {
        return settingsRepo.getCallForwardingAlways(token, userId)
    }

    override fun getCallForwardingNoAnswer(token: String, userId: String): Mono<CallForwardingNoAnswer> {
        return settingsRepo.getCallForwardingNoAnswer(token, userId)
    }

    override fun getCallForwardingBusy(token: String, userId: String): Mono<CallForwardingBusy> {
        return settingsRepo.getCallForwardingBusy(token, userId)
    }
}