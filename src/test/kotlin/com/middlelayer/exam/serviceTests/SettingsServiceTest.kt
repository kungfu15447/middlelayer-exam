package com.middlelayer.exam.serviceTests

import com.middlelayer.exam.core.interfaces.infrastructure.ISettingsRepository
import com.middlelayer.exam.core.models.ims.PresentationStatusEnum
import com.middlelayer.exam.service.SettingsService
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

}