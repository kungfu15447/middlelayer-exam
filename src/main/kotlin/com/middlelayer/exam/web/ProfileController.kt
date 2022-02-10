package com.middlelayer.exam.web
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.web.dto.profile.LoginDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono
import kotlin.math.log

@RestController
class ProfileController {
    private val profileService: IProfileService
    private val authService: IAuthService

    @Autowired
    constructor(profileService: IProfileService, authService: IAuthService) {
        this.profileService = profileService
        this.authService = authService
    }

    @PostMapping("api/user/profile")
    fun getProfile(@RequestBody loginDTO: LoginDTO) : Mono<ResponseEntity<Any>> {
        if (!loginDTO.username.isNullOrEmpty() && !loginDTO.password.isNullOrEmpty()) {
            val basicAuthToken = authService.createBasicAuthToken(loginDTO.username, loginDTO.password, true)
            val profile = profileService.getProfile(basicAuthToken, loginDTO.username)

            val response = profile.flatMap {
                val newToken = authService.createBasicAuthToken(it.userId, loginDTO.password)
                profileService.getServicesFromProfile(newToken, it.userId)
            }.zipWith(profile).flatMap {
                val profile = it.t2
                val services = it.t1
                val headers = HttpHeaders()
                headers.add("Authorization", "Bearer ${authService.register("authorization", profile, services)}")
                Mono.just(ResponseEntity<Any>(profile, headers,HttpStatus.OK))
            }
            return response
        }
        return Mono.just(ResponseEntity("Username and/or password cannot be null or empty", HttpStatus.BAD_REQUEST))
    }

    @GetMapping("user/test")
    fun getTest(): String {
        return "This is a test"
    }
}