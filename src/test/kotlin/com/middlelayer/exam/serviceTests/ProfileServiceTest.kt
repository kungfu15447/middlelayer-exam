package com.middlelayer.exam.serviceTests
import com.middlelayer.exam.core.models.domain.DProfile
import com.middlelayer.exam.service.ProfileService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono.just

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
    fun `GetProfile returns DomainProfile`() {
        val authentication = "test"
        val userid = "test"
        val expectedMessage = DProfile("","","","","","","")

        every {service.getProfile(authentication, userid)} returns just(expectedMessage)

        val result = service.getProfile(authentication, userid);
        result.subscribe {
            assertThat(it).isEqualTo(DProfile("","","","","","",""))
            verify { service.getProfile(authentication, userid) }
        }
    }

    @Test
    fun `getServicesFromProfile returns ListOfDomainServices`() {

    }
}