package com.middlelayer.exam.serviceTests

import com.middlelayer.exam.core.models.Profile
import com.middlelayer.exam.service.ProfileService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class ProfileServiceTest {

    @TestConfiguration
    class ProfileTestConfig {
        @Bean
        fun profileService() = mockk<ProfileService>()
    }

    @Autowired
    private lateinit var service: ProfileService



    @Test
    fun `GetProfile returns 200`() {
        val authentication = "test"
        val userid = "test"
        val expectedMessage = Profile()

        every {service.getProfile(authentication, userid)} returns expectedMessage

        val result = service.getProfile(authentication, userid);

        Assertions.assertThat(result).isEqualTo(Profile())
    }
}