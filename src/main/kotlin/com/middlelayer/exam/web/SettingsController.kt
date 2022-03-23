package com.middlelayer.exam.web

import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import com.middlelayer.exam.core.models.domain.*
import com.middlelayer.exam.core.models.ims.*
import com.middlelayer.exam.core.models.xsi.*
import com.middlelayer.exam.core.models.xsi.PersonalAssistant
import com.middlelayer.exam.web.dto.settings.*
import org.apache.coyote.Response
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

        val userSettings = settingsService.getSettings(basicToken, userId)

        return userSettings.flatMap {
            Mono.just(ResponseEntity(it, HttpStatus.OK))
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
        if (body.newNumber.isNullOrEmpty() || body.oldNumber.isNullOrEmpty()) {
            return Mono.just(ResponseEntity("New and/or old number cannot be null or empty", HttpStatus.BAD_REQUEST))
        }

        val claims = authService.getClaimsFromJWTToken(token)
        val userId = claims.profileObj.userId
        val basicToken = claims.basicToken

        val exclusionNumber = ExclusionNumber(
            body.newNumber
        )

        val updateExclusionNumber =
            settingsService.updateExclusionNumber(basicToken, userId, body.oldNumber, exclusionNumber)

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
        body.simRingLocations?.let { simRings ->
            simRingLocations = SimRingLocations(
                simRings.map { elem ->
                    SimRingLocation(
                        elem.address,
                        elem.answerConfirmedRequired
                    )
                }
            )
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
        val updateDoNotDisturb =
            settingsService.updateDoNotDisturb(claims.basicToken, claims.profileObj.userId, doNotDisturb)

        return updateDoNotDisturb.flatMap {
            Mono.just(ResponseEntity(HttpStatus.OK))
        }
    }

    @PutMapping("/callforwarding")
    fun updateCallForwarding(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: PutCallForwardDTO
    ): Mono<ResponseEntity<Any>> {
        val claims = authService.getClaimsFromJWTToken(token)
        val userId = claims.profileObj.userId
        val basicToken = claims.basicToken

        val always = body.always
        val busy = body.busy
        val noAnswer = body.noAnswer

        var listOfUpdates = ArrayList<Mono<Void>>()

        always?.let {
            val xsiAlways = CallForwardingAlways(
                it.active,
                it.phoneNumber
            )
            val update = settingsService.updateCallForwardingAlways(basicToken, userId, xsiAlways)
            listOfUpdates.add(update)
        }

        busy?.let {
            val xsiBusy = CallForwardingBusy(
                it.active,
                it.phoneNumber
            )
            val update = settingsService.updateCallForwardingBusy(basicToken, userId, xsiBusy)
            listOfUpdates.add(update)
        }

        noAnswer?.let {
            val xsiNoAnswer = CallForwardingNoAnswer(
                it.active,
                it.phoneNumber,
                it.numberOfRings
            )
            val update = settingsService.updateCallForwardingNoAnswer(basicToken, userId, xsiNoAnswer)
            listOfUpdates.add(update)
        }

        val response = Mono.zip(listOfUpdates) { }

        return response.then(
            Mono.just(ResponseEntity(HttpStatus.OK))
        )
    }

    @PutMapping("/numberdisplay")
    fun updateNumberDisplay(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: PutNumberDisplayDTO
    ): Mono<ResponseEntity<Any>> {
        val claims = authService.getClaimsFromJWTToken(token)
        val basicToken = claims.basicToken
        val userId = claims.profileObj.userId

        val displayStatus = stringToPresentationStatusEnum(body.presentationStatus)
        val numberDisplayHidden = NumberDisplayHidden(
            body.hideNumber
        )

        val updateHideNumber = settingsService.updateHideNumberStatus(basicToken, userId, numberDisplayHidden)
        val updateDisplayStatus = settingsService.updateNumberPresentationStatus(basicToken, userId, displayStatus)

        val response = Mono.zip(updateHideNumber, updateDisplayStatus)

        return response.then(
            Mono.just(ResponseEntity(HttpStatus.OK))
        )
    }

    @PutMapping("/remoteoffice")
    fun updateRemoteOffice(
        @RequestHeader("Authorization") token: String,
        @RequestBody body: PutRemoteOfficeDTO
    ): Mono<ResponseEntity<Any>> {
        if (body.remoteOfficeNumber.isNullOrEmpty()) {
            return Mono.just(ResponseEntity("Remote office number cannot be empty or null!", HttpStatus.BAD_REQUEST))
        }
        var claims = authService.getClaimsFromJWTToken(token)
        var remoteOffice = RemoteOffice(
            body.active,
            body.remoteOfficeNumber
        )
        return settingsService
            .updateRemoteOffice(claims.basicToken, claims.profileObj.userId, remoteOffice)
            .then(Mono.just(ResponseEntity(HttpStatus.OK)))
    }
}