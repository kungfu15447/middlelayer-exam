package com.middlelayer.exam.web

import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import com.middlelayer.exam.core.models.domain.*
import com.middlelayer.exam.core.models.xsi.*
import com.middlelayer.exam.web.dto.settings.GetSettingsResponseDTO
import com.middlelayer.exam.web.dto.settings.PutDoNotDisturbDTO
import com.middlelayer.exam.web.dto.settings.PutSimultaneousCallDTO
import com.middlelayer.exam.web.dto.settings.PutExclusionNumberDTO
import com.middlelayer.exam.web.dto.settings.PutPersonalAssistantDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

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

    @PutMapping("/personalassistant")
    fun updateStatus(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: PutPersonalAssistantDTO
    ): Mono<ResponseEntity<Any>> {
        val claims = authService.getClaimsFromJWTToken(token)
        val userId = claims.profileObj.userId
        val basicToken = claims.basicToken

        var toUpdateList = ArrayList<Mono<Void>>()

        val list = body.assignedCallToNumbers

        val xsiPA = PersonalAssistant(
            presence = stringToPresenceEnum(body.presence),
            enableExpirationTime = body.expirationTime != null,
            expirationTime = body.expirationTime,
            attendantNumber = body.transferNumber,
            ringSplash = body.transferCalls,
            enableTransferToAttendant = body.transferNotification
        )

        var updatePA = settingsService.updatePersonalAssistant(basicToken, userId, xsiPA)
        toUpdateList.add(updatePA)

        list?.let {
            var xsiList = it.map { item ->
                CallToNumber(stringToCallToNumberEnum(item))
            }
            var assignedCallToNumber = AssignedCallToNumbers(
                CallToNumberList(
                    xsiList
                )
            )
            var updateAssignedCTN =
                settingsService.updatePAAssignedCallToNumbers(basicToken, userId, assignedCallToNumber)
            toUpdateList.add(updateAssignedCTN)
        }

        val response = Mono.`when`(toUpdateList)

        return response.then(
            Mono.just(ResponseEntity(HttpStatus.OK))
        )
    }

    @PostMapping("/personalassistant/exclusionnumber/{number}")
    fun addExclusionNumber(
        @RequestHeader("Authorization") token: String,
        @PathVariable("number") phoneNumber: String
    ): Mono<ResponseEntity<Any>> {
        val claims = authService.getClaimsFromJWTToken(token)
        val userId = claims.profileObj.userId
        val basicToken = claims.basicToken

        if (phoneNumber.isNullOrEmpty()) {
            return Mono.just(ResponseEntity("Number to add cannot be empty or null", HttpStatus.BAD_REQUEST))
        }

        val exclusionNumber = ExclusionNumber(phoneNumber)

        var newExclusionNumber = settingsService.addExclusionNumber(basicToken, userId, exclusionNumber)

        return newExclusionNumber.then(
            Mono.just(ResponseEntity(HttpStatus.OK))
        )
    }

    @DeleteMapping("/personalassistant/exclusionnumber/{number}")
    fun deleteExclusionNumber(
        @RequestHeader("Authorization") token: String,
        @PathVariable("number") phoneNumber: String
    ): Mono<ResponseEntity<Any>> {
        val claims = authService.getClaimsFromJWTToken(token)
        val userId = claims.profileObj.userId
        val basicToken = claims.basicToken

        if (phoneNumber.isNullOrEmpty()) {
            return Mono.just(ResponseEntity("Number to add cannot be empty or null", HttpStatus.BAD_REQUEST))
        }

        val deleteExclusionNumber = settingsService.deleteExclusionNumber(basicToken, userId, phoneNumber)

        return deleteExclusionNumber.then(
            Mono.just(ResponseEntity(HttpStatus.OK))
        )
    }

    @PutMapping("/personalassistant/exclusionnumber")
    fun updateExclusionNumber(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: PutExclusionNumberDTO
    ): Mono<ResponseEntity<Any>> {
        val claims = authService.getClaimsFromJWTToken(token)
        val userId = claims.profileObj.userId
        val basicToken = claims.basicToken

        if (body.newNumber.isNullOrEmpty() || body.oldNumber.isNullOrEmpty()) {
            return Mono.just(ResponseEntity("New and/or old number cannot be null or empty", HttpStatus.BAD_REQUEST))
        }

        val exclusionNumber = ExclusionNumber(
            body.newNumber
        )

        val updateExclusionNumber = settingsService.updateExclusionNumber(basicToken, userId, body.oldNumber, exclusionNumber)

        return updateExclusionNumber.then(
            Mono.just(ResponseEntity(HttpStatus.OK))
        )
    }

    @PutMapping("/simultaneouscall")
    fun updateSimultaneousCall(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: PutSimultaneousCallDTO
    ): Mono<ResponseEntity<Any>> {
        val claims = authService.getClaimsFromJWTToken(token)
        var simRingLocations: SimRingLocations? = null
        body.simRingLocations?.let {
            if (body.simRingLocations.isNotEmpty()) {
                simRingLocations = SimRingLocations(
                    body.simRingLocations.map {
                        SimRingLocation(
                            it.address,
                            it.answerConfirmedRequired
                        )
                    }
                )
            }
        }

        var simultaneousRingPersonal = SimultaneousRingPersonal(
            body.active,
            if (body.doNotRingIfOnCall) IncomingCallsEnum.DoNotRing else IncomingCallsEnum.RingForAll,
            simRingLocations
        )
        return settingsService.updateSimultaneousRingPersonal(
            claims.basicToken,
            claims.profileObj.userId,
            simultaneousRingPersonal
        ).flatMap {
            Mono.just(ResponseEntity(HttpStatus.OK))
        }
    }

    @PutMapping("/donotdisturb")
    fun updateDoNotDisturb(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: PutDoNotDisturbDTO
    ): Mono<ResponseEntity<Any>> {
        val claims = authService.getClaimsFromJWTToken(token)
        val doNotDisturb = DoNotDisturb(
            body.active,
            body.ringSplash
        )
        val updateDoNotDisturb = settingsService.updateDoNotDisturb(claims.basicToken, claims.profileObj.userId, doNotDisturb)

        return updateDoNotDisturb.flatMap {
            Mono.just(ResponseEntity(HttpStatus.OK))
        }
    }
}