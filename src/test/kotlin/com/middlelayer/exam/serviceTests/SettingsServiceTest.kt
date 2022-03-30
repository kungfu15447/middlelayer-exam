package com.middlelayer.exam.serviceTests

import com.middlelayer.exam.core.interfaces.infrastructure.ISettingsRepository
import com.middlelayer.exam.core.models.ims.*
import com.middlelayer.exam.core.models.xsi.*
import com.middlelayer.exam.service.SettingsService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono
import org.mockito.kotlin.any as kAny

@ExtendWith(SpringExtension::class)
@SpringBootTest
class SettingsServiceTest(@Autowired val settingsService: SettingsService) {
    @MockBean
    private lateinit var settingsRepo: ISettingsRepository

    private fun getSettingsMockSetup() {
        `when`(settingsRepo.getCallForwardingAlways(kAny(), kAny())).thenReturn(
                Mono.just(
                        CallForwardingAlways(
                                active = false,
                                forwardToPhoneNumber = ""
                        )
                )
        )
        `when`(settingsRepo.getCallForwardingBusy(kAny(), kAny())).thenReturn(
                Mono.just(
                        CallForwardingBusy(
                                active = false,
                                forwardToPhoneNumber = ""
                        )
                )
        )
        `when`(settingsRepo.getCallForwardingNoAnswer(kAny(), kAny())).thenReturn(
                Mono.just(
                        CallForwardingNoAnswer(
                                active = false,
                                forwardToPhoneNumber = "",
                                numberOfRings = 1
                        )
                )
        )
        `when`(settingsRepo.getPAAssignedCallToNumbers(kAny(), kAny())).thenReturn(
                Mono.just(
                        AssignedCallToNumbers(
                                callToNumberList = CallToNumberList(
                                        callToNumber = emptyList()
                                )
                        )
                )
        )
        `when`(settingsRepo.getPAAvailableCallToNumbers(kAny(), kAny())).thenReturn(
                Mono.just(
                        AvailableCallToNumbers(
                                callToNumbers = emptyList()
                        )
                )
        )
        `when`(settingsRepo.getDoNotDisturb(kAny(), kAny())).thenReturn(
                Mono.just(
                        DoNotDisturb(
                                active = false,
                                ringSplash = false
                        )
                )
        )
        `when`(settingsRepo.getNumberDisplay(kAny(), kAny())).thenReturn(
                Mono.just(
                        NumberDisplay(
                                userId = "",
                                presentationStatus = PresentationStatusEnum.BUSINESS,
                                presentationNumber = "",
                                numberDescription = ""
                        )
                )
        )
        `when`(settingsRepo.getNumberDisplayStatus(kAny(), kAny())).thenReturn(
                Mono.just(
                        NumberDisplayHidden(
                                active =false
                        )
                )
        )
        `when`(settingsRepo.getPAExclusionNumbers(kAny(), kAny())).thenReturn(
                Mono.just(
                        emptyList()
                )
        )
        `when`(settingsRepo.getPersonalAssistant(kAny(), kAny())).thenReturn(
                Mono.just(
                        PersonalAssistant(
                                presence = PresenceEnum.NONE,
                                expirationTime = null,
                                numberOfRings = 5,
                                alertMeFirst = false,
                                attendantNumber = null,
                                callToNumberList = CallToNumberList(
                                        callToNumber = emptyList()
                                ),
                                enableExpirationTime = false,
                                enableTransferToAttendant = false,
                                ringSplash = false,
                        )
                )
        )
        `when`(settingsRepo.getMWIDeliveryToMobileEndpoint(kAny(), kAny())).thenReturn(
                Mono.just(
                        MWIDeliveryToMobileEndpoint(
                                active = false,
                                mobilePhoneNumber = ""
                        )
                )
        )
        `when`(settingsRepo.getVoiceMessaging(kAny(), kAny())).thenReturn(
                Mono.just(
                        VoiceMessaging(
                                active = false,
                                alwaysRedirectToVoiceMail = false,
                                busyRedirectToVoiceMail = false,
                                noAnswerRedirectToVoiceMail = false
                        )
                )
        )
        `when`(settingsRepo.getVoiceMessagingGreeting(kAny(), kAny())).thenReturn(
                Mono.just(
                        VoiceMessagingGreeting(
                               noAnswerNumberOfRings = 0
                        )
                )
        )
        `when`(settingsRepo.getRemoteOffice(kAny(), kAny())).thenReturn(
                Mono.just(
                        RemoteOffice(
                                active = false,
                                remoteOfficeNumber = ""
                        )
                )
        )
        `when`(settingsRepo.getSimultaneousRingPersonal(kAny(), kAny())).thenReturn(
                Mono.just(
                        SimultaneousRingPersonal(
                                active = false,
                                incomingCalls = IncomingCallsEnum.DoNotRing,
                                simRingLocations = SimRingLocations(
                                        emptyList()
                                )
                        )
                )
        )
    }

    @Test
    fun `on getSettings success calls getCallForwardingBusy once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getCallForwardingBusy(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getCallForwardingAlways once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getCallForwardingAlways(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getCallForwardingNoAnswer once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getCallForwardingNoAnswer(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getNumberDisplay once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getNumberDisplay(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getDoNotDisturb once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getDoNotDisturb(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getMWIDeliveryToMobileEndpoint once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getMWIDeliveryToMobileEndpoint(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getNumberDisplayStatus once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getNumberDisplayStatus(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getPAAssignedCallToNumbers once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getPAAssignedCallToNumbers(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getPAAvailableCallToNumbers once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getPAAvailableCallToNumbers(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getPAExclusionNumbers once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getPAExclusionNumbers(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getPersonalAssistant once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getPersonalAssistant(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getRemoteOffice once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getRemoteOffice(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getSimultaneousRingPersonal once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getSimultaneousRingPersonal(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getVoiceMessaging once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getVoiceMessaging(kAny(), kAny())
    }

    @Test
    fun `on getSettings success calls getVoiceMessagingGreeting once`() {
        //Assign
        getSettingsMockSetup()

        //Act
        settingsService
                .getSettings("", "")
                .block()

        //Assert
        verify(settingsRepo, times(1)).getVoiceMessagingGreeting(kAny(), kAny())
    }

    @Test
    fun `on updateNumberPresentationStatus with MOBILE param calls updatePresentationToMobile once`() {
        //Assign
        `when`(settingsRepo.updatePresentationToMobile(kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsRepo.updatePresentationToBusiness(kAny(), kAny())).thenReturn(Mono.empty())

        //Act
        settingsService
            .updateNumberPresentationStatus("", "", PresentationStatusEnum.MOBILE)
            .block()


        //Assert
        verify(settingsRepo, times(1)).updatePresentationToMobile(kAny(), kAny())
    }


    @Test
    fun `on updateNumberPresentationStatus with MOBILE param does not call updatePresentationToBusiness`() {
        //Assign
        `when`(settingsRepo.updatePresentationToMobile(kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsRepo.updatePresentationToBusiness(kAny(), kAny())).thenReturn(Mono.empty())

        //Act
        settingsService
            .updateNumberPresentationStatus("", "", PresentationStatusEnum.MOBILE)
            .block()


        //Assert
        verify(settingsRepo, times(0)).updatePresentationToBusiness(kAny(), kAny())
    }

    @Test
    fun `on updateNumberPresentationStatus with BUSINESS param calls updatePresentationToBusiness once`() {
        //Assign
        `when`(settingsRepo.updatePresentationToMobile(kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsRepo.updatePresentationToBusiness(kAny(), kAny())).thenReturn(Mono.empty())

        //Act
        settingsService
            .updateNumberPresentationStatus("", "", PresentationStatusEnum.BUSINESS)
            .block()


        //Assert
        verify(settingsRepo, times(1)).updatePresentationToBusiness(kAny(), kAny())
    }

    @Test
    fun `on updateNumberPresentationStatus with BUSINESS param does not call updatePresentationToMobile`() {
        //Assign
        `when`(settingsRepo.updatePresentationToMobile(kAny(), kAny())).thenReturn(Mono.empty())
        `when`(settingsRepo.updatePresentationToBusiness(kAny(), kAny())).thenReturn(Mono.empty())

        //Act
        settingsService
            .updateNumberPresentationStatus("", "", PresentationStatusEnum.BUSINESS)
            .block()


        //Assert
        verify(settingsRepo, times(0)).updatePresentationToMobile(kAny(), kAny())
    }
}