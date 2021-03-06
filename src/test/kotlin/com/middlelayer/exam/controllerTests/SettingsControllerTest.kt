package com.middlelayer.exam.controllerTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import com.middlelayer.exam.core.models.auth.ProfileTokenObject
import com.middlelayer.exam.core.models.auth.TokenClaimsObject
import com.middlelayer.exam.core.models.domain.*
import com.middlelayer.exam.core.models.ims.NumberDisplay
import com.middlelayer.exam.core.models.xsi.*
import com.middlelayer.exam.helpers.WebHeader
import com.middlelayer.exam.helpers.WebTestHelper
import com.middlelayer.exam.web.SettingsController
import com.middlelayer.exam.web.dto.settings.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import org.mockito.kotlin.any as kAny

@ExtendWith(SpringExtension::class)
@WebFluxTest(
        controllers = [SettingsController::class],
        excludeAutoConfiguration = [ReactiveSecurityAutoConfiguration::class]
)
class SettingsControllerTest(@Autowired val webTestClient: WebTestClient) {
    @MockBean
    private lateinit var settingsService: ISettingsService

    @MockBean
    private lateinit var authService: IAuthService

    private lateinit var json: ObjectMapper

    private lateinit var web: WebTestHelper

    @BeforeEach
    fun setup() {
        json = ObjectMapper()
        web = WebTestHelper(webTestClient)
    }

    private fun getSettingsMockSetup() {
        `when`(settingsService.getSettings(kAny(), kAny())).thenReturn(Mono.just(
                UserSettings(
                        personalAssistant = PersonalAssistantSettings(
                                presence = "Presence",
                                expiration = null,
                                transferCalls = false,
                                transferNumber = null,
                                transferNotification = false,
                                assignedNumbers = emptyList(),
                                availableNumbers = emptyList(),
                                exclusionNumbers = emptyList()
                        ),
                        remoteOffice = RemoteOfficeSettings(RemoteOffice()),
                        callForwarding = CallForwardingSettings(
                                always = CallForwardingAlwaysSettings(
                                        CallForwardingAlways()
                                ),
                                busy = CallForwardingBusySettings(
                                        CallForwardingBusy()
                                ),
                                noAnswer = CallForwardingNoAnswerSettings(
                                        CallForwardingNoAnswer()
                                )
                        ),
                        numberDisplay = NumberDisplaySettings(
                                NumberDisplay()
                        ),
                        voicemail = VoicemailSettings(
                                active = false,
                                alwaysRedirectToVoiceMail = false,
                                busyRedirectToVoiceMail = false,
                                noAnswerRedirectToVoiceMail = false,
                                noAnswerNumberOfRings = 0,
                                sendPushNotification = false
                        ),
                        simultaneousCall = SimultaneousCallSettings(
                                simultaneousRingPersonal = SimultaneousRingPersonal()
                        ),
                        doNotDisturb = false,
                        blocked = false,
                )
        ))
    }

    private fun getClaimsMockSetup() {
        `when`(authService.getClaimsFromJWTToken(kAny())).thenReturn(
                TokenClaimsObject(
                        services = emptyList(),
                        profileObj = ProfileTokenObject(
                                userId = "UserId",
                                groupId = "GroupId",
                                fullName = "FullName",
                                email = "Email"
                        ),
                        basicToken = "BasicToken"
                )
        )
    }

    @Test
    fun `on GET Settings success return OK status result`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        val response = web.get(
                "/api/user/settings",
                arrayListOf(WebHeader("Authorization", "someToken"))
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on GET Settings success calls getSettings once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        web.get(
                "/api/user/settings",
                arrayListOf(WebHeader("Authorization", "someToken"))
        )
                .returnResult(String::class.java)
                .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getSettings(kAny(), kAny())
    }

    @Test
    fun `on GET Settings success calls getClaimsFromJWTToken once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        web.get(
                "/api/user/settings",
                arrayListOf(WebHeader("Authorization", "someToken"))
        )
                .returnResult(String::class.java)
                .responseBody.blockFirst()

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT SimultaneousCall success returns OK status result`() {
        //Assign
        `when`(settingsService.updateSimultaneousRingPersonal(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutSimultaneousCallDTO(
                active = false,
                doNotRingIfOnCall = true,
                simRingLocations = null
        )

        //Act
        val response = web.put(
                "/api/user/settings/simultaneouscall",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on PUT SimultaneousCall success calls getClaimsFromJWTToken once`() {
        //Assign
        `when`(settingsService.updateSimultaneousRingPersonal(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutSimultaneousCallDTO(
                active = false,
                doNotRingIfOnCall = true,
                simRingLocations = null
        )

        //Act
        web.put(
                "/api/user/settings/simultaneouscall",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody.blockFirst()

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT SimultaneousCall success calls updateSimultaneousRingPersonal once`() {
        //Assign
        `when`(settingsService.updateSimultaneousRingPersonal(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutSimultaneousCallDTO(
                active = false,
                doNotRingIfOnCall = true,
                simRingLocations = null
        )

        //Act
        web.put(
                "/api/user/settings/simultaneouscall",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).updateSimultaneousRingPersonal(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT SimultaneousCall on empty body returns Bad Request status result`() {
        //Assign
        `when`(settingsService.updateSimultaneousRingPersonal(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()

        //Act
        val response = web.put(
                "/api/user/settings/simultaneouscall",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                )
        )

        //Assert
        response.expectStatus().isBadRequest
    }

    @Test
    fun `on PUT DoNotDisturb success returns OK Status Result`() {
        //Assign
        `when`(settingsService.updateDoNotDisturb(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutDoNotDisturbDTO(
                active = false,
                ringSplash = false,
        )

        //Act
        val response = web.put(
                "/api/user/settings/donotdisturb",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on PUT DoNotDisturb success calls updateDoNotDisturb once`() {
        //Assign
        `when`(settingsService.updateDoNotDisturb(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutDoNotDisturbDTO(
                active = false,
                ringSplash = false,
        )

        //Act
        web.put(
                "/api/user/settings/donotdisturb",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).updateDoNotDisturb(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT DoNotDisturb success calls getClaimsFromJWTToken once`() {
        //Assign
        `when`(settingsService.updateDoNotDisturb(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutDoNotDisturbDTO(
                active = false,
                ringSplash = false,
        )

        //Act
        web.put(
                "/api/user/settings/donotdisturb",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT DoNotDisturb on empty body returns Bad Request Status Result`() {
        //Assign
        `when`(settingsService.updateDoNotDisturb(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()

        //Act
        val response = web.put(
                "/api/user/settings/donotdisturb",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
        )

        //Assert
        response.expectStatus().isBadRequest
    }

    @Test
    fun `on PUT RemoteOffice success return OK status result`() {
        //Assign
        `when`(settingsService.updateRemoteOffice(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutRemoteOfficeDTO(
                active = false,
                remoteOfficeNumber = "111"
        )

        //Act
        val response = web.put(
                "/api/user/settings/remoteoffice",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on PUT RemoteOffice on success calls updateRemoteOffice once`() {
        //Assign
        `when`(settingsService.updateRemoteOffice(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutRemoteOfficeDTO(
                active = false,
                remoteOfficeNumber = "111"
        )

        //Act
        web.put(
                "/api/user/settings/remoteoffice",
                arrayListOf(
                        WebHeader("Authorization", "someToken")
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).updateRemoteOffice(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT RemoteOffice on success calls getClaimsFromJWTToken once`() {
        //Assign
        `when`(settingsService.updateRemoteOffice(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutRemoteOfficeDTO(
                active = false,
                remoteOfficeNumber = "111"
        )

        //Act
        web.put(
                "/api/user/settings/remoteoffice",
                arrayListOf(
                        WebHeader("Authorization", "someToken")
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT RemoteOffice with empty number in body returns Bad Request status result`() {
        //Assign
        `when`(settingsService.updateRemoteOffice(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutRemoteOfficeDTO(
                active = false,
                remoteOfficeNumber = ""
        )

        //Act
        val response = web.put(
                "/api/user/settings/remoteoffice",
                arrayListOf(
                        WebHeader("Authorization", "someToken")
                ),
                body
        )

        //Assert
        response.expectStatus().isBadRequest
    }

    @Test
    fun `on PUT RemoteOffice with empty number does not call updateRemoteOffice`() {
        //Assign
        `when`(settingsService.updateRemoteOffice(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutRemoteOfficeDTO(
                active = false,
                remoteOfficeNumber = ""
        )

        //Act
        web.put(
                "/api/user/settings/remoteoffice",
                arrayListOf(
                        WebHeader("Authorization", "someToken")
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(0)).updateRemoteOffice(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT RemoteOffice with empty number does not call getClaimsFromJWTToken`() {
        //Assign
        `when`(settingsService.updateRemoteOffice(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutRemoteOfficeDTO(
                active = false,
                remoteOfficeNumber = ""
        )

        //Act
        web.put(
                "/api/user/settings/remoteoffice",
                arrayListOf(
                        WebHeader("Authorization", "someToken")
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(authService, times(0)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT RemoteOffice with empty body returns Bad Request status result`() {
        //Assign
        `when`(settingsService.updateRemoteOffice(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()

        //Act
        val response = web.put(
                "/api/user/settings/personalassistant/exclusionnumber",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
        )

        //Assert
        response.expectStatus().isBadRequest
    }

    @Test
    fun `on PUT CallForwarding on success returns OK status result`() {
        //Assign
        `when`(settingsService.updateCallForwardingAlways(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingBusy(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingNoAnswer(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutCallForwardDTO(
                always = PutCallForwardAlways(
                        active = true,
                        phoneNumber = "111"
                ),
                busy = PutCallForwardBusy(
                        active = true,
                        phoneNumber = "111"
                ),
                noAnswer = PutCallForwardNoAnswer(
                        active = false,
                        phoneNumber = "111",
                        numberOfRings = 1
                )
        )

        //Act
        val response = web.put(
                "/api/user/settings/callforwarding",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on PUT CallForwarding on success calls updateCallForwardingAlways once`() {
        //Assign
        `when`(settingsService.updateCallForwardingAlways(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingBusy(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingNoAnswer(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutCallForwardDTO(
                always = PutCallForwardAlways(
                        active = true,
                        phoneNumber = "111"
                ),
                busy = PutCallForwardBusy(
                        active = true,
                        phoneNumber = "111"
                ),
                noAnswer = PutCallForwardNoAnswer(
                        active = false,
                        phoneNumber = "111",
                        numberOfRings = 1
                )
        )

        //Act
        web.put(
                "/api/user/settings/callforwarding",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).updateCallForwardingAlways(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT CallForwarding on success calls updateCallForwardingNoAnswer once`() {
        //Assign
        `when`(settingsService.updateCallForwardingAlways(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingBusy(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingNoAnswer(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutCallForwardDTO(
                always = PutCallForwardAlways(
                        active = true,
                        phoneNumber = "111"
                ),
                busy = PutCallForwardBusy(
                        active = true,
                        phoneNumber = "111"
                ),
                noAnswer = PutCallForwardNoAnswer(
                        active = false,
                        phoneNumber = "111",
                        numberOfRings = 1
                )
        )

        //Act
        web.put(
                "/api/user/settings/callforwarding",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).updateCallForwardingNoAnswer(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT CallForwarding on success calls updateCallForwardingBusy once`() {
        //Assign
        `when`(settingsService.updateCallForwardingAlways(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingBusy(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingNoAnswer(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutCallForwardDTO(
                always = PutCallForwardAlways(
                        active = true,
                        phoneNumber = "111"
                ),
                busy = PutCallForwardBusy(
                        active = true,
                        phoneNumber = "111"
                ),
                noAnswer = PutCallForwardNoAnswer(
                        active = false,
                        phoneNumber = "111",
                        numberOfRings = 1
                )
        )

        //Act
        web.put(
                "/api/user/settings/callforwarding",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).updateCallForwardingBusy(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT CallForwarding when PutCallForwardAlways is null then never calls updateCallForwardingAlways`() {
        //Assign
        `when`(settingsService.updateCallForwardingAlways(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingBusy(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingNoAnswer(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutCallForwardDTO(
                always = null,
                busy = PutCallForwardBusy(
                        active = true,
                        phoneNumber = "111"
                ),
                noAnswer = PutCallForwardNoAnswer(
                        active = false,
                        phoneNumber = "111",
                        numberOfRings = 1
                )
        )
        web.put(
                "/api/user/settings/remoteoffice",
                arrayListOf(
                        WebHeader("Authorization", "someToken")
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(0)).updateCallForwardingAlways(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT CallForwarding when PutCallForwardBusy is null then never calls updateCallForwardingBusy`() {
        //Assign
        `when`(settingsService.updateCallForwardingAlways(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingBusy(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingNoAnswer(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutCallForwardDTO(
                always = PutCallForwardAlways(
                        active = true,
                        phoneNumber = "111"
                ),
                busy = null,
                noAnswer = PutCallForwardNoAnswer(
                        active = false,
                        phoneNumber = "111",
                        numberOfRings = 1
                )
        )

        //Act
        web.put(
                "/api/user/settings/callforwarding",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(0)).updateCallForwardingBusy(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT CallForwarding when PutCallForwardNoAnswer is null then never calls updateCallForwardingNoAnswer`() {
        //Assign
        `when`(settingsService.updateCallForwardingAlways(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingBusy(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingNoAnswer(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutCallForwardDTO(
                always = PutCallForwardAlways(
                        active = true,
                        phoneNumber = "111"
                ),
                busy = PutCallForwardBusy(
                        active = true,
                        phoneNumber = "111"
                ),
                noAnswer = null
        )

        //Act
        web.put(
                "/api/user/settings/callforwarding",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(0)).updateCallForwardingNoAnswer(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT CallForwarding on success calls getClaimsFromJWTToken once`() {
        //Assign
        `when`(settingsService.updateCallForwardingAlways(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingBusy(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingNoAnswer(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutCallForwardDTO(
                always = PutCallForwardAlways(
                        active = true,
                        phoneNumber = "111"
                ),
                busy = PutCallForwardBusy(
                        active = true,
                        phoneNumber = "111"
                ),
                noAnswer = PutCallForwardNoAnswer(
                        active = false,
                        phoneNumber = "111",
                        numberOfRings = 1
                )
        )

        //Act
        web.put(
                "/api/user/settings/callforwarding",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT CallForwarding if request is null then return Bad Request status result`() {
        //Assign
        `when`(settingsService.updateCallForwardingAlways(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingBusy(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingNoAnswer(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()

        //Act
        val response = web.put(
                "/api/user/settings/callforwarding",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                )
        )

        //Assert
        response.expectStatus().isBadRequest
    }

    @Test
    fun `on PUT CallForwarding if every variable in request is null then return Ok status result`() {
        //Assign
        `when`(settingsService.updateCallForwardingAlways(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingBusy(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateCallForwardingNoAnswer(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutCallForwardDTO(
                always = null,
                busy = null,
                noAnswer = null
        )

        //Act
        val response = web.put(
                "/api/user/settings/callforwarding",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on PUT NumberDisplay success returns OK Status Result`() {
        //Assign
        `when`(settingsService.updateHideNumberStatus(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateNumberPresentationStatus(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutNumberDisplayDTO(
                hideNumber = false,
                presentationStatus = "Mobile"
        )

        //Act
        val response = web.put(
                "/api/user/settings/numberdisplay",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on PUT NumberDisplay success calls updateNumberPresentationStatus once`() {
        //Assign
        `when`(settingsService.updateHideNumberStatus(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateNumberPresentationStatus(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutNumberDisplayDTO(
                hideNumber = false,
                presentationStatus = "Mobile"
        )

        //Act
        web.put(
                "/api/user/settings/numberdisplay",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).updateNumberPresentationStatus(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT NumberDisplay success calls updateHideNumberStatus once`() {
        //Assign
        `when`(settingsService.updateHideNumberStatus(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateNumberPresentationStatus(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutNumberDisplayDTO(
                hideNumber = false,
                presentationStatus = "Mobile"
        )

        web.put(
                "/api/user/settings/numberdisplay",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).updateHideNumberStatus(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT NumberDisplay success calls getClaimsFromJWTToken once`() {
        //Assign
        `when`(settingsService.updateHideNumberStatus(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updateNumberPresentationStatus(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutNumberDisplayDTO(
                hideNumber = false,
                presentationStatus = "Mobile"
        )

        web.put(
                "/api/user/settings/numberdisplay",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT PersonalAssistant on success returns OK status result`() {
        //Assign
        `when`(settingsService.updatePersonalAssistant(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updatePAAssignedCallToNumbers(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutPersonalAssistantDTO(
                presence = "None",
                expirationTime = null,
                transferNumber = null,
                transferCalls = true,
                transferNotification = true,
                assignedCallToNumbers = arrayListOf(
                        "1",
                        "2",
                        "3"
                )
        )

        //Act
        val response = web.put(
                "/api/user/settings/personalassistant",
                arrayListOf(
                        WebHeader("Authorization", "someToken")
                ),
                body
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on PUT PersonalAssistant on success calls updatePersonalAssistant once`() {
        //Assign
        `when`(settingsService.updatePersonalAssistant(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updatePAAssignedCallToNumbers(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutPersonalAssistantDTO(
                presence = "None",
                expirationTime = null,
                transferNumber = null,
                transferCalls = true,
                transferNotification = true,
                assignedCallToNumbers = arrayListOf(
                        "1",
                        "2",
                        "3"
                )
        )

        //Act
        web.put(
                "/api/user/settings/personalassistant",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).updatePersonalAssistant(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT PersonalAssistant on success calls updatePAAssignedCallToNumbers once`() {
        //Assign
        `when`(settingsService.updatePersonalAssistant(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updatePAAssignedCallToNumbers(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutPersonalAssistantDTO(
                presence = "None",
                expirationTime = null,
                transferNumber = null,
                transferCalls = true,
                transferNotification = true,
                assignedCallToNumbers = arrayListOf(
                        "1",
                        "2",
                        "3"
                )
        )

        //Act
        web.put(
                "/api/user/settings/personalassistant",
                arrayListOf(
                        WebHeader("Authorization", "someToken")
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).updatePAAssignedCallToNumbers(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT PersonalAssistant on success calls getClaimsFromJWTToken once`() {
        //Assign
        `when`(settingsService.updatePersonalAssistant(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updatePAAssignedCallToNumbers(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutPersonalAssistantDTO(
                presence = "None",
                expirationTime = null,
                transferNumber = null,
                transferCalls = true,
                transferNotification = true,
                assignedCallToNumbers = arrayListOf(
                        "1",
                        "2",
                        "3"
                )
        )

        //Act
        web.put(
                "/api/user/settings/personalassistant",
                arrayListOf(
                        WebHeader("Authorization", "someToken")
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT PersonalAssistant if assignedCallToNumbers in request is null then do not call updatePAAssignedCallToNumbers`() {
        //Assign
        `when`(settingsService.updatePersonalAssistant(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updatePAAssignedCallToNumbers(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutPersonalAssistantDTO(
                presence = "None",
                expirationTime = null,
                transferNumber = null,
                transferCalls = true,
                transferNotification = true,
                assignedCallToNumbers = null
        )

        //Act
        web.put(
                "/api/user/settings/personalassistant",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(0)).updatePAAssignedCallToNumbers(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT PersonalAssistant if request body is null then return Bad Request status result`() {
        //Assign
        `when`(settingsService.updatePersonalAssistant(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updatePAAssignedCallToNumbers(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()

        //Act
        val response = web.put(
                "/api/user/settings/personalassistant",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
        )

        //Assert
        response.expectStatus().isBadRequest
    }

    @Test
    fun `on POST ExclusionNumber on success then return OK status result`() {
        //Assign
        `when`(settingsService.addExclusionNumber(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val number = "123"

        //Act
        val response = web.post(
                "/api/user/settings/personalassistant/exclusionnumber/${number}",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on POST ExclusionNumber on success calls addExclusionNumber once`() {
        //Assign
        `when`(settingsService.addExclusionNumber(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val number = "123"

        //Act
        web.post(
                "/api/user/settings/personalassistant/exclusionnumber/${number}",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).addExclusionNumber(kAny(), kAny(), kAny())
    }

    @Test
    fun `on POST ExclusionNumber on success calls getClaimsFromJWTToken once`() {
        //Assign
        `when`(settingsService.addExclusionNumber(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val number = "123"

        //Act
        web.post(
                "/api/user/settings/personalassistant/exclusionnumber/${number}",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on DELETE ExclusionNumber on success then return OK status result`() {
        //Assign
        `when`(settingsService.deleteExclusionNumber(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val number = "123"

        //Act
        val response = web.delete(
                "/api/user/settings/personalassistant/exclusionnumber/${number}",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on DELETE ExclusionNumber on success calls deleteExclusionNumber once`() {
        //Assign
        `when`(settingsService.deleteExclusionNumber(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val number = "123"

        //Act
        web.delete(
                "/api/user/settings/personalassistant/exclusionnumber/${number}",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).deleteExclusionNumber(kAny(), kAny(), kAny())
    }

    @Test
    fun `on DELETE ExclusionNumber on success calls getClaimsFromJWTToken once`() {
        //Assign
        `when`(settingsService.deleteExclusionNumber(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val number = "123"

        //Act
        web.delete(
                "/api/user/settings/personalassistant/exclusionnumber/${number}",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT ExclusionNumber on success then return OK status result`() {
        //Assign
        `when`(settingsService.updateExclusionNumber(kAny(), kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutExclusionNumberDTO(
                "123",
                "1234"
        )

        //Act
        val response = web.put(
                "/api/user/settings/personalassistant/exclusionnumber",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on PUT ExclusionNumber on success calls updateExclusionNumber once`() {
        //Assign
        `when`(settingsService.updateExclusionNumber(kAny(), kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutExclusionNumberDTO(
                "123",
                "1234"
        )

        //Act
        web.put(
                "/api/user/settings/personalassistant/exclusionnumber",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(1)).updateExclusionNumber(kAny(), kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT ExclusionNumber on success calls getClaimsFromJWTToken once`() {
        //Assign
        `when`(settingsService.updateExclusionNumber(kAny(), kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutExclusionNumberDTO(
                "123",
                "1234"
        )

        //Act
        web.put(
                "/api/user/settings/personalassistant/exclusionnumber",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @ParameterizedTest
    @CsvSource(
            "'',123",
            "123,''",
            "'',''"
    )
    fun `on PUT ExclusionNumber if oldNumber or newNumber in request body is null or empty then return Bad Request status result`(
            oldNumber: String,
            newNumber: String
    ) {
        //Assign
        `when`(settingsService.updateExclusionNumber(kAny(), kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutExclusionNumberDTO(
                oldNumber,
                newNumber
        )

        //Act
        val response = web.put(
                "/api/user/settings/personalassistant/exclusionnumber",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )

        //Assert
        response.expectStatus().isBadRequest
    }

    @ParameterizedTest
    @CsvSource(
            "'',123",
            "123,''",
            "'',''"
    )
    fun `on PUT ExclusionNumber if oldNumber or newNumber in request body is null or empty then never calls updateExclusionNumber`(
            oldNumber: String,
            newNumber: String
    ) {
        //Assign
        `when`(settingsService.updateExclusionNumber(kAny(), kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutExclusionNumberDTO(
                oldNumber,
                newNumber
        )

        //Act
        web.put(
                "/api/user/settings/personalassistant/exclusionnumber",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(settingsService, times(0)).updateExclusionNumber(kAny(), kAny(), kAny(), kAny())
    }

    @ParameterizedTest
    @CsvSource(
            "'',123",
            "123,''",
            "'',''"
    )
    fun `on PUT ExclusionNumber if oldNumber or newNumber in request body is null or empty then never calls getClaimsFromJWTToken`(
            oldNumber: String,
            newNumber: String
    ) {
        //Assign
        `when`(settingsService.updateExclusionNumber(kAny(), kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        val body = PutExclusionNumberDTO(
                oldNumber,
                newNumber
        )

        //Act
        web.put(
                "/api/user/settings/personalassistant/exclusionnumber",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
                body
        )
                .returnResult(String::class.java)
                .responseBody
                .blockFirst()

        //Assert
        verify(authService, times(0)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT ExclusionNumber with empty body then returns Bad Request status result`() {
        //Assign
        `when`(settingsService.updateExclusionNumber(kAny(), kAny(), kAny(), kAny())).thenReturn(Mono.empty())

        //Act
        val response = web.put(
                "/api/user/settings/personalassistant/exclusionnumber",
                arrayListOf(
                        WebHeader("Authorization", "someToken"),
                ),
        )

        //Assert
        response.expectStatus().isBadRequest
    }
}