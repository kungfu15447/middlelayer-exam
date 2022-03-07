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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
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
            Mono.just(DCallForwardingAlways(
                false,
                ""
                )
            )
        )
        `when`(settingsService.getCallForwardingBusy(kAny(), kAny())).thenReturn(
            Mono.just(DCallForwardingBusy(
                false,
                ""
                )
            )
        )
        `when`(settingsService.getCallForwardingNoAnswer(kAny(), kAny())).thenReturn(
            Mono.just(DCallForwardingNoAnswer(
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
            Mono.just(DDoNotDisturb(
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
}