package com.middlelayer.exam.web

import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import com.middlelayer.exam.core.models.domain.*
import com.middlelayer.exam.core.models.xsi.*
import com.middlelayer.exam.web.dto.settings.GetSettingsResponseDTO
import com.middlelayer.exam.web.dto.settings.PersonalAssistantPut
import com.middlelayer.exam.web.dto.settings.PutSettingsStatusDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("api/user/settings")
class SettingsController {
    private val settingsService: ISettingsService
    private val authService: IAuthService

    @Autowired
    constructor(settingsService: ISettingsService, authService: IAuthService) {
        this.settingsService = settingsService
        this.authService = authService
    }

    @GetMapping("")
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
        val voiceMessagingGreeting = settingsService.getVoiceMessagingGreeting(basicToken, userId)
        val pushNotification = settingsService.getPushNotification(basicToken, userId)
        val simultaneousRing = settingsService.getSimultaneousRing(basicToken, userId)
        val doNotDisturb = settingsService.getDoNotDisturb(basicToken, userId)

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

        val response = Mono.zip(
            personalAssistantZip,
            remoteOffice,
            numberDisplayZip,
            callForwardZip,
            voiceMessagingZip,
            simultaneousRing,
            doNotDisturb
        )
        return response.flatMap {
            val paZip = it.t1
            val ndZip = it.t3
            val cfZip = it.t4
            val vmZip = it.t5

            //Personal Assistant settings
            val pa: DPersonalAssistant = paZip.t1
            val en: List<DExclusionNumber> = paZip.t2
            val asctn: List<DCallToNumber> = paZip.t3
            val avctn: List<DCallToNumber> = paZip.t4

            //Number Display settings
            val ndh: DNumberDisplayHidden = ndZip.t1
            val nd: DNumberDisplay = ndZip.t2

            //Remote Office settings
            val ro: DRemoteOffice = it.t2

            //Call Forwarding settings
            val cfa: DCallForwardingAlways = cfZip.t1
            val cfb: DCallForwardingBusy = cfZip.t2
            val cfna: DCallForwardingNoAnswer = cfZip.t3

            //Voice Messaging settings
            val vm: DVoiceMessaging = vmZip.t1
            val vmg: DVoiceMessagingGreeting = vmZip.t2
            val pn: DPushNotification = vmZip.t3

            //Simultaneous Ring settings
            val sr: DSimultaneousRing = it.t6

            //Do Not Disturbs settings
            val dnd: DDoNotDisturb = it.t7

            val responseBody = GetSettingsResponseDTO(
                pa,
                asctn,
                avctn,
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
                dnd
            )
            Mono.just(ResponseEntity(responseBody, HttpStatus.OK))
        }
    }

    @PutMapping("/status")
    fun updateStatus(@RequestHeader("Authorization") token: String, @RequestBody body: PutSettingsStatusDTO): Mono<ResponseEntity<Any>> {
        val claims = authService.getClaimsFromJWTToken(token)
        val userId = claims.profileObj.userId
        val basicToken = claims.basicToken

        var updatePA = Mono.just<Optional<Void>>(Optional.empty())
        var updateActn = Mono.just<Optional<Void>>(Optional.empty())
        var updateEn = Mono.just<Optional<Void>>(Optional.empty())
        var addEn = Mono.just<Optional<Void>>(Optional.empty())
        var deleteEn = Mono.just<Optional<Void>>(Optional.empty())

        val paPut = body.personalAssistance
        val enPost = body.newExclusionNumber
        val enPut = body.updateExclusionNumber
        val enDelete = body.exclusionNumberToDelete

        paPut?.let {
            val personalAssistant = PersonalAssistant(
                presence = it.presence,
                enableExpirationTime = it.enableExpirationTime,
                expirationTime = it.expirationTime,
                enableTransferToAttendant = it.enableTransferToAttendant,
                alertMeFirst = it.alertMeFirst,
                attendantNumber = it.attendantNumber,
                ringSplash = it.ringSplash,
                callToNumberList = CallToNumberList(it.callToNumberList.map { ctnPut ->
                    CallToNumber(ctnPut.type, ctnPut.alternateNumberId)
                }),
                numberOfRings = it.numberOfRings
            )

            val assignedCallToNumbers = AssignedCallToNumbers(
                callToNumberList = CallToNumberList(it.callToNumberList.map { ctnPut ->
                    CallToNumber(ctnPut.type, ctnPut.alternateNumberId)
                })
            )

            updatePA = settingsService.updatePersonalAssistant(basicToken, userId, personalAssistant).flatMap {
                Mono.just(Optional.of(it))
            }
            updateActn = settingsService.updatePAAssignedCallToNumbers(basicToken, userId, assignedCallToNumbers).flatMap {
                Mono.just(Optional.of(it))
            }
        }

        enPost?.let {
            val newExclusionNumber = ExclusionNumber(
                it
            )
            addEn = settingsService.addExclusionNumber(basicToken, userId, newExclusionNumber).flatMap {
                Mono.just(Optional.of(it))
            }
        }

        enPut?.let {
            val updatedExclusionNumber = ExclusionNumber(
                enPut.newNumber
            )
            updateEn = settingsService.updateExclusionNumber(basicToken, userId, it.oldNumber, updatedExclusionNumber).flatMap {
                Mono.just(Optional.of(it))
            }
        }

        enDelete?.let {
            deleteEn = settingsService.deleteExclusionNumber(basicToken, userId, it).flatMap {
                Mono.just(Optional.of(it))
            }
        }

        var updateZip = Mono.zip(
            updatePA,
            updateActn,
            addEn,
            updateEn,
            deleteEn
        )

        return updateZip.flatMap {
            Mono.just(ResponseEntity(HttpStatus.OK))
        }
    }

    @PutMapping("/callforwarding")
    fun updateCallForwarding(): Mono<ResponseEntity<Any>> {
        return Mono.just(ResponseEntity(HttpStatus.OK))
    }
}