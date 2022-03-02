package com.middlelayer.exam.controllerTests
import com.fasterxml.jackson.databind.ObjectMapper
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.core.models.domain.DProfile
import com.middlelayer.exam.core.models.domain.DService
import com.middlelayer.exam.web.ProfileController
import com.middlelayer.exam.web.dto.profile.LoginDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@ExtendWith(SpringExtension::class)
class ProfileControllerTest {
    private lateinit var web: WebTestClient

    @MockBean
    private lateinit var profileService: IProfileService

    @MockBean
    private lateinit var authService: IAuthService

    private lateinit var json: ObjectMapper

    @BeforeEach
    fun setup() {
        json = ObjectMapper()
        web = WebTestClient
            .bindToController(ProfileController(profileService, authService))
            .build()
    }

    @ParameterizedTest
    @CsvSource(
        ",",
        "'',''",
        "username,",
        ",password",
    )
    fun `on Login with invalid body returns bad request`(username: String?, password: String?) {
        //Assign
        val requestBody = LoginDTO(username, password)

        //Act
        var response = web.post()
            .uri("/api/user/profile/login")
            .body(BodyInserters.fromValue(requestBody))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()

        //Assert
        response.expectStatus().isBadRequest
    }

    @ParameterizedTest
    @CsvSource(
        ",",
        "'',''",
        "username,",
        ",password",
    )
    fun `on Login with invalid body returns correct error message`(username: String?, password: String?) {
        //Assign
        val requestBody = LoginDTO(username, password)

        //Act
        var response = web.post()
            .uri("/api/user/profile/login")
            .body(BodyInserters.fromValue(requestBody))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        assert(response == "Username and/or password cannot be null or empty")
    }

    @ParameterizedTest
    @CsvSource(
        ",",
        "'',''",
        "username,",
        ",password",
    )
    fun `on Login with invalid body never calls getProfile`(username: String?, password: String?) {
        //Assign
        val requestBody = LoginDTO(username, password)

        //Act
        var response = web.post()
            .uri("/api/user/profile/login")
            .body(BodyInserters.fromValue(requestBody))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(profileService, times(0)).getProfile("", "")
    }

    @ParameterizedTest
    @CsvSource(
        ",",
        "'',''",
        "username,",
        ",password",
    )
    fun `on Login with invalid body never calls getServicesFromProfile`(username: String?, password: String?) {
        //Assign
        val requestBody = LoginDTO(username, password)

        //Act
        var response = web.post()
            .uri("/api/user/profile/login")
            .body(BodyInserters.fromValue(requestBody))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(profileService, times(0)).getServicesFromProfile("", "")
    }

    @ParameterizedTest
    @CsvSource(
        ",",
        "'',''",
        "username,",
        ",password",
    )
    fun `on Login with invalid body never calls createBasicAuthToken`(username: String?, password: String?) {
        //Assign
        val requestBody = LoginDTO(username, password)

        //Act
        var response = web.post()
            .uri("/api/user/profile/login")
            .body(BodyInserters.fromValue(requestBody))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(authService, times(0)).createBasicAuthToken("", "")
    }

    @ParameterizedTest
    @CsvSource(
        ",",
        "'',''",
        "username,",
        ",password",
    )
    fun `on Login with invalid body never calls register`(username: String?, password: String?) {
        //Assign
        val requestBody = LoginDTO(username, password)

        //Act
        var response = web.post()
            .uri("/api/user/profile/login")
            .body(BodyInserters.fromValue(requestBody))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        val profile = DProfile("", "", "", "", "", "", "")
        var services = ArrayList<DService>()
        verify(authService, times(0)).register("", profile, services)
    }
}