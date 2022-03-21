package com.middlelayer.exam.controllerTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.ISettingsService
import com.middlelayer.exam.core.models.auth.ProfileTokenObject
import com.middlelayer.exam.core.models.auth.TokenClaimsObject
import com.middlelayer.exam.core.models.domain.*
import com.middlelayer.exam.helpers.WebHeader
import com.middlelayer.exam.helpers.WebTestHelper
import com.middlelayer.exam.web.SettingsController
import com.middlelayer.exam.web.dto.settings.PutExclusionNumberDTO
import com.middlelayer.exam.web.dto.settings.PutPersonalAssistantDTO
import com.middlelayer.exam.web.dto.settings.PutRemoteOfficeDTO
import com.middlelayer.exam.web.dto.settings.PutSimultaneousCallDTO
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
        `when`(settingsService.getCallForwardingAlways(kAny(), kAny())).thenReturn(
            Mono.just(
                DCallForwardingAlways(
                    false,
                    ""
                )
            )
        )
        `when`(settingsService.getCallForwardingBusy(kAny(), kAny())).thenReturn(
            Mono.just(
                DCallForwardingBusy(
                    false,
                    ""
                )
            )
        )
        `when`(settingsService.getCallForwardingNoAnswer(kAny(), kAny())).thenReturn(
            Mono.just(
                DCallForwardingNoAnswer(
                    false,
                    "",
                    1
                )
            )
        )
        `when`(settingsService.getPAAssignedCallToNumbers(kAny(), kAny())).thenReturn(
            Mono.just(emptyList())
        )
        `when`(settingsService.getPAAvailableCallToNumbers(kAny(), kAny())).thenReturn(
            Mono.just(emptyList())
        )
        `when`(settingsService.getDoNotDisturb(kAny(), kAny())).thenReturn(
            Mono.just(
                DDoNotDisturb(
                    false
                )
            )
        )
        `when`(settingsService.getNumberDisplay(kAny(), kAny())).thenReturn(
            Mono.just(
                DNumberDisplay(
                    "",
                    ""
                )
            )
        )
        `when`(settingsService.getNumberDisplayStatus(kAny(), kAny())).thenReturn(
            Mono.just(
                DNumberDisplayHidden(
                    false
                )
            )
        )
        `when`(settingsService.getPAExclusionNumbers(kAny(), kAny())).thenReturn(
            Mono.just(emptyList())
        )
        `when`(settingsService.getPersonalAssistant(kAny(), kAny())).thenReturn(
            Mono.just(
                DPersonalAssistant(
                    "",
                    false,
                    null,
                    false,
                    "",
                    false,
                    emptyList()
                )
            )
        )
        `when`(settingsService.getPushNotification(kAny(), kAny())).thenReturn(
            Mono.just(
                DPushNotification(
                    false,
                    ""
                )
            )
        )
        `when`(settingsService.getVoiceMessaging(kAny(), kAny())).thenReturn(
            Mono.just(
                DVoiceMessaging(
                    false,
                    false,
                    false,
                    false
                )
            )
        )
        `when`(settingsService.getVoiceMessagingGreeting(kAny(), kAny())).thenReturn(
            Mono.just(
                DVoiceMessagingGreeting(
                    1
                )
            )
        )
        `when`(settingsService.getRemoteOffice(kAny(), kAny())).thenReturn(
            Mono.just(
                DRemoteOffice(
                    false,
                    ""
                )
            )
        )
        `when`(settingsService.getSimultaneousRing(kAny(), kAny())).thenReturn(
            Mono.just(
                DSimultaneousRing(
                    false,
                    false,
                    emptyList()
                )
            )
        )
    }

    private fun getClaimsMockSetup() {
        `when`(authService.getClaimsFromJWTToken(kAny())).thenReturn(
            TokenClaimsObject(
                emptyList(),
                ProfileTokenObject(
                    "",
                    "",
                    "",
                    ""
                ),
                ""
            )
        )
    }

    @Test
    fun `on get Settings success return OK status result`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on get Settings success calls getCallForwardingAlways once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getCallForwardingAlways(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getCallForwardingBusy once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getCallForwardingBusy(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getCallForwardingNoAnswer once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getCallForwardingNoAnswer(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getPersonalAssistant once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getPersonalAssistant(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getNumberDisplay once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getNumberDisplay(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getDoNotDisturb once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getDoNotDisturb(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getNumberDisplayStatus once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getNumberDisplayStatus(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getPAAssignedCallToNumbers once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getPAAssignedCallToNumbers(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getPAAvailableCallToNumbers once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getPAAvailableCallToNumbers(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getPAExclusionNumbers once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getPAExclusionNumbers(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getPushNotification once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getPushNotification(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getRemoteOffice once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getRemoteOffice(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getVoiceMessagingGreeting once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getVoiceMessagingGreeting(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getSimultaneousRing once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getSimultaneousRing(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getVoiceMessaging once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
            "/api/user/settings",
            arrayListOf(WebHeader("Authorization", "someToken"))
        )
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(settingsService, times(1)).getVoiceMessaging(kAny(), kAny())
    }

    @Test
    fun `on get Settings success calls getClaimsFromJWTToken once`() {
        //Assign
        getSettingsMockSetup()
        getClaimsMockSetup()

        //Act
        var response = web.get(
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
        var body = PutSimultaneousCallDTO(
            active = false,
            doNotRingIfOnCall = true,
            simRingLocations = null
        )

        //Act
        var response = web.put(
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
        var body = PutSimultaneousCallDTO(
            active = false,
            doNotRingIfOnCall = true,
            simRingLocations = null
        )

        //Act
        var response = web.put(
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
        var body = PutSimultaneousCallDTO(
            active = false,
            doNotRingIfOnCall = true,
            simRingLocations = null
        )

        //Act
        var response = web.put(
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
        var response = web.put(
            "/api/user/settings/simultaneouscall",
            arrayListOf(
                WebHeader("Authorization", "someToken"),
            )
        )

        //Assert
        response.expectStatus().isBadRequest
    }

    @Test
    fun `on PUT RemoteOffice success return OK status result`() {
        //Assign
        `when`(settingsService.updateRemoteOffice(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        var body = PutRemoteOfficeDTO(
            active = false,
            remoteOfficeNumber = "111"
        )

        //Act
        var response = web.put(
            "/api/user/settings/remoteoffice",arrayListOf(
                WebHeader("Authorization", "someToken")
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
        var body = PutRemoteOfficeDTO(
            active = false,
            remoteOfficeNumber = "111"
        )

        //Act
        var response = web.put(
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
        var body = PutRemoteOfficeDTO(
            active = false,
            remoteOfficeNumber = "111"
        )

        //Act
        var response = web.put(
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
        var body = PutRemoteOfficeDTO(
            active = false,
            remoteOfficeNumber = ""
        )

        //Act
        var response = web.put(
            "/api/user/settings/remoteoffice",arrayListOf(
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
        var body = PutRemoteOfficeDTO(
            active = false,
            remoteOfficeNumber = ""
        )

        //Act
        var response = web.put(
            "/api/user/settings/remoteoffice",
            arrayListOf(
                WebHeader("Authorization", "someToken")
            ),
            body
        )

        //Assert
        verify(settingsService, times(0)).updateRemoteOffice(kAny(), kAny(), kAny())
    }

    @Test
    fun `on PUT RemoteOffice with empty number does not call getClaimsFromJWTToken`() {
        //Assign
        `when`(settingsService.updateRemoteOffice(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        var body = PutRemoteOfficeDTO(
            active = false,
            remoteOfficeNumber = ""
        )

        //Act
        var response = web.put(
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
        var response = web.put(
            "/api/user/settings/personalassistant/exclusionnumber",
            arrayListOf(
                WebHeader("Authorization", "someToken"),
            ),
        )

        //Assert
        response.expectStatus().isBadRequest
    }

    @Test
    fun `on PUT PersonalAssistant on success returns OK status result`() {
        //Assign
        `when`(settingsService.updatePersonalAssistant(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updatePAAssignedCallToNumbers(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        var body = PutPersonalAssistantDTO(
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
        var response = web.put(
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
        var body = PutPersonalAssistantDTO(
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
        var response = web.put(
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
        var body = PutPersonalAssistantDTO(
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
        var response = web.put(
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
        var body = PutPersonalAssistantDTO(
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
        var response = web.put(
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
        verify(authService, times(1)).getClaimsFromJWTToken(kAny())
    }

    @Test
    fun `on PUT PersonalAssistant if assignedCallToNumbers in request is null then do not call updatePAAssignedCallToNumbers`() {
        //Assign
        `when`(settingsService.updatePersonalAssistant(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsService.updatePAAssignedCallToNumbers(kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        var body = PutPersonalAssistantDTO(
                presence = "None",
                expirationTime = null,
                transferNumber = null,
                transferCalls = true,
                transferNotification = true,
                assignedCallToNumbers = null
        )

        //Act
        var response = web.put(
            "/api/user/settings/personalassistant",
            arrayListOf(
                WebHeader("Authorization", "someToken"),
            ),
            body
        )


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
        var response = web.put(
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
        var number = "123"

        //Act
        var response = web.post(
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
        var number = "123"

        //Act
        var response = web.post(
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
        var number = "123"

        //Act
        var response = web.post(
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
        var number = "123"

        //Act
        var response = web.delete(
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
        var number = "123"

        //Act
        var response = web.delete(
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
        var number = "123"

        //Act
        var response = web.delete(
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
        var body = PutExclusionNumberDTO(
                "123",
                "1234"
        )

        //Act
        var response = web.put(
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
        var body = PutExclusionNumberDTO(
                "123",
                "1234"
        )

        //Act
        var response = web.put(
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
        var body = PutExclusionNumberDTO(
                "123",
                "1234"
        )

        //Act
        var response = web.put(
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

    @ParameterizedTest
    @CsvSource(
            "'',123",
            "123,''",
            "'',''"
    )
    fun `on PUT ExclusionNumber if oldNumber or newNumber in request body is null or empty then return Bad Request status result`(oldNumber: String, newNumber: String) {
        //Assign
        `when`(settingsService.updateExclusionNumber(kAny(), kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        var body = PutExclusionNumberDTO(
                oldNumber,
                newNumber
        )

        //Act
        var response = web.put(
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
    fun `on PUT ExclusionNumber if oldNumber or newNumber in request body is null or empty then never calls updateExclusionNumber`(oldNumber: String, newNumber: String) {
        //Assign
        `when`(settingsService.updateExclusionNumber(kAny(), kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        var body = PutExclusionNumberDTO(
                oldNumber,
                newNumber
        )

        //Act
        var response = web.put(
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
    fun `on PUT ExclusionNumber if oldNumber or newNumber in request body is null or empty then never calls getClaimsFromJWTToken`(oldNumber: String, newNumber: String) {
        //Assign
        `when`(settingsService.updateExclusionNumber(kAny(), kAny(), kAny(), kAny())).thenReturn(Mono.empty())
        getClaimsMockSetup()
        var body = PutExclusionNumberDTO(
                oldNumber,
                newNumber
        )

        //Act
        var response = web.put(
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
        var response = web.put(
            "/api/user/settings/personalassistant/exclusionnumber",
            arrayListOf(
                WebHeader("Authorization", "someToken"),
            ),
        )

        //Assert
        response.expectStatus().isBadRequest
    }
}