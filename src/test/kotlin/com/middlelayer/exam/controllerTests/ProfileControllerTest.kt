package com.middlelayer.exam.controllerTests
import com.middlelayer.exam.web.ProfileController
import com.middlelayer.exam.web.dto.profile.LoginDTO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.core.publisher.Mono.just

@ExtendWith(SpringExtension::class)
class ProfileControllerTest {

    @TestConfiguration
    class ProfileTestConfig {
        @Bean
        fun profileController() = mockk<ProfileController>()
    }

    @Autowired
        private lateinit var profileController: ProfileController


    @Test
    fun `getProfile returnsStatus`() {
        val loginDTO = LoginDTO("username","password")
        val response: ResponseEntity<Any> = ResponseEntity<Any>(HttpStatus.OK)

        every { profileController.getProfile(loginDTO)} returns just(response)

        val result = profileController.getProfile(loginDTO);

        result.subscribe {
            assertThat(it).isEqualTo(ResponseEntity<Any>(HttpStatus.OK))
            verify { profileController.getProfile(loginDTO) }
        }
    }
}