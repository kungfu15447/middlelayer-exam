package com.middlelayer.exam.controllerTests

import com.fasterxml.jackson.databind.ObjectMapper
import com.middlelayer.exam.core.exceptions.BadRequestException
import com.middlelayer.exam.core.exceptions.ISEException
import com.middlelayer.exam.core.exceptions.NotFoundException
import com.middlelayer.exam.core.exceptions.UnauthorizedException
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.core.models.domain.DService
import com.middlelayer.exam.helpers.WebTestHelper
import com.middlelayer.exam.web.ErrorControllerAdvice
import com.middlelayer.exam.web.ProfileController
import com.middlelayer.exam.web.dto.profile.LoginDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@WebFluxTest(
    controllers = [
        ProfileController::class,
        ErrorControllerAdvice::class
    ],
    excludeAutoConfiguration = [ReactiveSecurityAutoConfiguration::class]
)
class ErrorControllerAdviceTest(@Autowired val webTestClient: WebTestClient) {
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

    @Test
    fun `Throws UnauthorizedException returns Unauthorized status result`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val services = ArrayList<DService>()

        `when`(profileService.getProfile(any(), any())).thenAnswer {
            throw UnauthorizedException("Something went wrong")
        }
        `when`(profileService.getServicesFromProfile(any(), any())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(any(), any())).thenReturn("basicToken")
        `when`(authService.register(any(), any(), any())).thenReturn("jwtToken")

        //Act
        var response = web.post("/api/user/profile/login", requestBody)

        //Assert
        response.expectStatus().isUnauthorized
    }

    @Test
    fun `Throws UnauthorizedException returns correct error message`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val services = ArrayList<DService>()
        val ex = UnauthorizedException("Something went wrong")

        `when`(profileService.getProfile(any(), any())).thenAnswer {
            throw ex
        }
        `when`(profileService.getServicesFromProfile(any(), any())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(any(), any())).thenReturn("basicToken")
        `when`(authService.register(any(), any(), any())).thenReturn("jwtToken")

        //Act
        var response = web.post("/api/user/profile/login", requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        assert(response == ex.message)
    }

    @Test
    fun `Throws NotFoundException returns Not Found status result`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val services = ArrayList<DService>()

        `when`(profileService.getProfile(any(), any())).thenAnswer {
            throw NotFoundException("Something went wrong")
        }
        `when`(profileService.getServicesFromProfile(any(), any())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(any(), any())).thenReturn("basicToken")
        `when`(authService.register(any(), any(), any())).thenReturn("jwtToken")

        //Act
        var response = web.post("/api/user/profile/login", requestBody)

        //Assert
        response.expectStatus().isNotFound
    }

    @Test
    fun `Throws NotFoundException returns correct error message`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val services = ArrayList<DService>()
        val ex = NotFoundException("Something went wrong")

        `when`(profileService.getProfile(any(), any())).thenAnswer {
            throw ex
        }
        `when`(profileService.getServicesFromProfile(any(), any())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(any(), any())).thenReturn("basicToken")
        `when`(authService.register(any(), any(), any())).thenReturn("jwtToken")

        //Act
        var response = web.post("/api/user/profile/login", requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        assert(response == ex.message)
    }

    @Test
    fun `Throws ISEException returns Internal Server Error status result`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val services = ArrayList<DService>()
        val ex = ISEException("Something went wrong")

        `when`(profileService.getProfile(any(), any())).thenAnswer {
            throw ex
        }
        `when`(profileService.getServicesFromProfile(any(), any())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(any(), any())).thenReturn("basicToken")
        `when`(authService.register(any(), any(), any())).thenReturn("jwtToken")

        //Act
        var response = web.post("/api/user/profile/login", requestBody)

        //Assert
        response.expectStatus().is5xxServerError
    }

    @Test
    fun `Throws ISEException returns correct error message`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val services = ArrayList<DService>()
        val ex = ISEException("Something went wrong")

        `when`(profileService.getProfile(any(), any())).thenAnswer {
            throw ex
        }
        `when`(profileService.getServicesFromProfile(any(), any())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(any(), any())).thenReturn("basicToken")
        `when`(authService.register(any(), any(), any())).thenReturn("jwtToken")

        //Act
        var response = web.post("/api/user/profile/login", requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        assert(response == ex.message)
    }

    @Test
    fun `Throws BadRequestException returns Bad Request status result`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val services = ArrayList<DService>()
        val ex = BadRequestException("Something went wrong")

        `when`(profileService.getProfile(any(), any())).thenAnswer {
            throw ex
        }
        `when`(profileService.getServicesFromProfile(any(), any())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(any(), any())).thenReturn("basicToken")
        `when`(authService.register(any(), any(), any())).thenReturn("jwtToken")

        //Act
        var response = web.post("/api/user/profile/login", requestBody)

        //Assert
        response.expectStatus().isBadRequest
    }

    @Test
    fun `Throws BadRequestException returns correct error message`() {
        //Assign
        val requestBody = LoginDTO("username", "password")
        val services = ArrayList<DService>()
        val ex = BadRequestException("Something went wrong")

        `when`(profileService.getProfile(any(), any())).thenAnswer {
            throw ex
        }
        `when`(profileService.getServicesFromProfile(any(), any())).thenReturn(Mono.just(services))
        `when`(authService.createBasicAuthToken(any(), any())).thenReturn("basicToken")
        `when`(authService.register(any(), any(), any())).thenReturn("jwtToken")

        //Act
        var response = web.post("/api/user/profile/login", requestBody)
            .returnResult(String::class.java)
            .responseBody.blockFirst()

        //Assert
        assert(response == ex.message)
    }
}