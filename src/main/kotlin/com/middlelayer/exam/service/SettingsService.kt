package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.infrastructure.ISettingsRepository
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import com.middlelayer.exam.core.models.domain.DCallToNumber
import com.middlelayer.exam.core.models.xsi.CallToNumberList
import com.middlelayer.exam.core.models.xsi.ExclusionNumber
import com.middlelayer.exam.core.models.xsi.PersonalAssistant
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

    override fun getPaAssignedCallToNumbers(token: String, userId: String): Mono<List<DCallToNumber>> {
        return settingsRepo.getPAAssignedCallToNumbers(token, userId).flatMap {
            Mono.just(it.callToNumberList.callToNumbers.map { ctn ->
                DCallToNumber(ctn.type ?: "")
            })
        }
    }
}