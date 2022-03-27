package com.middlelayer.exam.controllerTests
import com.fasterxml.jackson.databind.ObjectMapper
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.core.models.auth.BasicTokenObject
import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service
import com.middlelayer.exam.helpers.WebTestHelper
import com.middlelayer.exam.web.ProfileController
import com.middlelayer.exam.web.dto.profile.LoginDTO
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
    controllers = [ProfileController::class],
    excludeAutoConfiguration = [ReactiveSecurityAutoConfiguration::class]
)
class ProfileControllerTest(@Autowired val webTestClient: WebTestClient) {
    @MockBean
    private lateinit var profileService: IProfileService

    @MockBean
    private lateinit var authService: IAuthService

    private lateinit var json: ObjectMapper

    private lateinit var web: WebTestHelper

    @BeforeEach
    fun setup() {
        json = ObjectMapper()
        web = WebTestHelper(webTestClient)
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
        val response = web.post("/api/user/profile/login", body = requestBody)

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
        val response = web.post("/api/user/profile/login", body = requestBody)
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
        web.post("/api/user/profile/login", body = requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(profileService, times(0)).getProfile(kAny(), kAny())
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
        web.post("/api/user/profile/login", body = requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(profileService, times(0)).getServicesFromProfile(kAny(), kAny())
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
        web.post("/api/user/profile/login", body = requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(authService, times(0)).createBasicAuthToken(kAny(), kAny())
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
        web.post("/api/user/profile/login", body = requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(authService, times(0)).register(kAny(), kAny(), kAny())
    }

    @ParameterizedTest
    @CsvSource(
            ",",
            "'',''",
            "username,",
            ",password",
    )
    fun `on Login with invalid body never calls getCredentialsFromBasicToken`(username: String?, password: String?) {
        //Assign
        val requestBody = LoginDTO(username, password)

        //Act
        web.post("/api/user/profile/login", body = requestBody)
                .returnResult(String::class.java)
                .responseBody.blockFirst()

        //Assert
        verify(authService, times(0)).getCredentialsFromBasicToken(kAny())
    }

    @Test
    fun `on Login success returns OK status`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val profile = Profile()
        val services = ArrayList<Service>()

        `when`(profileService.getProfile(kAny(), kAny())).thenReturn(Mono.just(profile))
        `when`(profileService.getServicesFromProfile(kAny(), kAny())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(kAny(), kAny())).thenReturn("basicToken")
        `when`(authService.register(kAny(), kAny(), kAny())).thenReturn("jwtToken")
        `when`(authService.getCredentialsFromBasicToken(kAny())).thenReturn(BasicTokenObject("username", "password"))

        //Act
        val response = web.post("/api/user/profile/login", body = requestBody)

        //Assert
        response.expectStatus().isOk
    }

    @Test
    fun `on Login success calls getProfile once`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val profile = Profile()
        val services = ArrayList<Service>()

        `when`(profileService.getProfile(kAny(), kAny())).thenReturn(Mono.just(profile))
        `when`(profileService.getServicesFromProfile(kAny(), kAny())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(kAny(), kAny())).thenReturn("basicToken")
        `when`(authService.register(kAny(), kAny(), kAny())).thenReturn("jwtToken")
        `when`(authService.getCredentialsFromBasicToken(kAny())).thenReturn(BasicTokenObject("username", "password"))

        //Act
        web.post("/api/user/profile/login", body = requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(profileService, times(1)).getProfile(kAny(), kAny())
    }

    @Test
    fun `on Login success calls getServicesFromProfile once`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val profile = Profile()
        val services = ArrayList<Service>()

        `when`(profileService.getProfile(kAny(), kAny())).thenReturn(Mono.just(profile))
        `when`(profileService.getServicesFromProfile(kAny(), kAny())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(kAny(), kAny())).thenReturn("basicToken")
        `when`(authService.register(kAny(), kAny(), kAny())).thenReturn("jwtToken")
        `when`(authService.getCredentialsFromBasicToken(kAny())).thenReturn(BasicTokenObject("username", "password"))

        //Act
        web.post("/api/user/profile/login", body = requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(profileService, times(1)).getServicesFromProfile(kAny(), kAny())
    }

    @Test
    fun `on Login success calls createBasicAuthToken twice`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val profile = Profile()
        val services = ArrayList<Service>()

        `when`(profileService.getProfile(kAny(), kAny())).thenReturn(Mono.just(profile))
        `when`(profileService.getServicesFromProfile(kAny(), kAny())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(kAny(), kAny())).thenReturn("basicToken")
        `when`(authService.register(kAny(), kAny(), kAny())).thenReturn("jwtToken")
        `when`(authService.getCredentialsFromBasicToken(kAny())).thenReturn(BasicTokenObject("username", "password"))

        //Act
        web.post("/api/user/profile/login", body = requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(authService, times(2)).createBasicAuthToken(kAny(), kAny())
    }

    @Test
    fun `on Login success calls getCredentialsFromBasicToken once`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val profile = Profile()
        val services = ArrayList<Service>()

        `when`(profileService.getProfile(kAny(), kAny())).thenReturn(Mono.just(profile))
        `when`(profileService.getServicesFromProfile(kAny(), kAny())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(kAny(), kAny())).thenReturn("basicToken")
        `when`(authService.register(kAny(), kAny(), kAny())).thenReturn("jwtToken")
        `when`(authService.getCredentialsFromBasicToken(kAny())).thenReturn(BasicTokenObject("username", "password"))

        //Act
        web.post("/api/user/profile/login", body = requestBody)
                .returnResult(String::class.java)
                .responseBody.blockFirst()

        //Assert
        verify(authService, times(1)).getCredentialsFromBasicToken(kAny())
    }

    @Test
    fun `on Login success calls register once`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val profile = Profile()
        val services = ArrayList<Service>()

        `when`(profileService.getProfile(kAny(), kAny())).thenReturn(Mono.just(profile))
        `when`(profileService.getServicesFromProfile(kAny(), kAny())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(kAny(), kAny())).thenReturn("basicToken")
        `when`(authService.register(kAny(), kAny(), kAny())).thenReturn("jwtToken")
        `when`(authService.getCredentialsFromBasicToken(kAny())).thenReturn(BasicTokenObject("username", "password"))

        //Act
        web.post("/api/user/profile/login", body = requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        verify(authService, times(1)).register(kAny(), kAny(), kAny())
    }
}