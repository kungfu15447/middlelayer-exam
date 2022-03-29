package com.middlelayer.exam.web
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.web.dto.profile.LoginDTO
import com.middlelayer.exam.web.dto.profile.LoginDTOResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/user/profile")
class ProfileController {
    private val profileService: IProfileService
    private val authService: IAuthService

    @Autowired
    constructor(profileService: IProfileService, authService: IAuthService) {
        this.profileService = profileService
        this.authService = authService
    }

    @SecurityRequirements
    @PostMapping("/login")
    fun getProfile(@RequestBody loginDTO: LoginDTO) : Mono<ResponseEntity<Any>> {
        var someCode = 2
        if (someCode > 3) {
            println("No way")
        }
        if (!loginDTO.username.isNullOrEmpty() && !loginDTO.password.isNullOrEmpty()) {
            val basicAuthToken = authService.createBasicAuthToken(loginDTO.username, loginDTO.password)
            val credentials = authService.getCredentialsFromBasicToken(basicAuthToken)
            val profile = profileService.getProfile(basicAuthToken, credentials.username)

            val response = profile.flatMap { profile ->
                val userId = profile.details.userId ?: ""

                val newToken = authService.createBasicAuthToken(userId, loginDTO.password)
                profileService.getServicesFromProfile(newToken, userId).flatMap { services ->
                    var jwt = authService.register(newToken, profile, services)
                    Mono.just(ResponseEntity<Any>(LoginDTOResponse(jwt, profile, services), HttpStatus.OK))
                }
            }
            return response
        } else {
            return Mono.just(ResponseEntity("Username and/or password cannot be null or empty", HttpStatus.BAD_REQUEST))
        }
    }
}