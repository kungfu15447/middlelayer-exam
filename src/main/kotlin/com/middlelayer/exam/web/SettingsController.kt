package com.middlelayer.exam.web

import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import com.middlelayer.exam.core.models.domain.DCallToNumber
import com.middlelayer.exam.core.models.ims.NumberDisplay
import com.middlelayer.exam.core.models.xsi.*
import com.middlelayer.exam.web.dto.settings.GetSettingsResponseDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class SettingsController {
    private val settingsService: ISettingsService
    private val authService: IAuthService

    @Autowired
    constructor(settingsService: ISettingsService, authService: IAuthService) {
        this.settingsService = settingsService
        this.authService = authService
    }

    @GetMapping("api/user/settings")
    fun getSettings(@RequestHeader("Authorization") token: String): Mono<ResponseEntity<Any>> {
        val claims = authService.getClaimsFromJWTToken(token)
        val userId = claims.profileObj.userId
        val basicToken = claims.basicToken

        val personalAssistant = settingsService.getPersonalAssistant(basicToken, userId)
        val exclusionNumbers = settingsService.getPAExclusionNumbers(basicToken, userId)
        val assignedCallToNumbers = settingsService.getPAAssignedCallToNumbers(basicToken, userId)
        val availableCallToNumbers = settingsService.getPAAvailableCallToNumbers(basicToken, userId)
        val remoteOffice = settingsService.getRemoteOffice(basicToken, userId)
        val numberDisplayStatus = settingsService.getNumberDisplayStatus(basicToken, userId)
        val numberDisplay = settingsService.getNumberDisplay(basicToken, userId)
        val callForwardingAlways = settingsService.getCallForwardingAlways(basicToken, userId)
        val callForwardingBusy = settingsService.getCallForwardingBusy(basicToken, userId)
        val callForwardingNoAnswer = settingsService.getCallForwardingNoAnswer(basicToken, userId)
        val voiceMessaging = settingsService.getVoiceMessaging(basicToken, userId)

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
            voiceMessaging
        )

        val response = Mono.zip(
            personalAssistantZip,
            remoteOffice,
            numberDisplayZip,
            callForwardZip,
            voiceMessagingZip
        )
        return response.flatMap {
            val paZip = it.t1
            val ndZip = it.t3
            val cfZip = it.t4
            val vmZip = it.t5

            //Personal Assistant settings
            val pa: PersonalAssistant = paZip.t1
            val en: List<ExclusionNumber> = paZip.t2
            val asctn: List<DCallToNumber> = paZip.t3
            val avctn: List<DCallToNumber> = paZip.t4

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

            val responseBody = GetSettingsResponseDTO(
                pa,
                en,
                asctn,
                avctn,
                ro,
                ndh,
                nd
            )
            Mono.just(ResponseEntity(responseBody, HttpStatus.OK))
        }
    }
}