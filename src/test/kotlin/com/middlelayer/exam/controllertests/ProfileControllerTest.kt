package com.middlelayer.exam.controllertests

import com.middlelayer.exam.service.AuthService
import com.middlelayer.exam.service.ProfileService
import io.mockk.mockk
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ProfileControllerTest {

    @TestConfiguration
    class ProfileTestConfig {
        @Bean
        fun profileService() = mockk<ProfileService>()
        fun authService() = mockk<AuthService>()
    }

    @Autowired
    private lateinit var profileservice: ProfileService
    private lateinit var authService: ProfileService: mProfileService
}