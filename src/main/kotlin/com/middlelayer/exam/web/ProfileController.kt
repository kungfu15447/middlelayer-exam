package com.middlelayer.exam.web
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.IProfileService
import com.middlelayer.exam.web.dto.profile.LoginDTO
import okhttp3.internal.format
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import reactor.core.publisher.Mono
import kotlin.math.log

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

    @PostMapping("/login")
    fun getProfile(@RequestBody loginDTO: LoginDTO) : Mono<ResponseEntity<Any>> {
        if (!loginDTO.username.isNullOrEmpty() && !loginDTO.password.isNullOrEmpty()) {
            val formattedUsername = formatUsername(loginDTO.username)
            val basicAuthToken = authService.createBasicAuthToken(formattedUsername, loginDTO.password)
            val profile = profileService.getProfile(basicAuthToken, formattedUsername)

            val response = profile.flatMap { profile->
                val newToken = authService.createBasicAuthToken(profile.userId, loginDTO.password)
                profileService.getServicesFromProfile(newToken, profile.userId).flatMap { s ->
                    val headers = HttpHeaders()
                    headers.add("Authorization", "Bearer ${authService.register(newToken, profile, s)}")
                    Mono.just(ResponseEntity<Any>(profile, headers,HttpStatus.OK))
                }
            }
            return response
        } else {
            return Mono.just(ResponseEntity("Username and/or password cannot be null or empty", HttpStatus.BAD_REQUEST))
        }
    }

    @GetMapping("/test")
    fun getTest(): String {
        return "This is a test"
    }

    private fun formatUsername(username: String): String {
        var formattedUserName = username
            .replace("+45", "")
            .replace(" ", "")

        //Is username numeric?
        if (formattedUserName.matches(Regex("[0-9]+"))) {
            formattedUserName = "PA_45$formattedUserName"
        }

        return formattedUserName
    }
}