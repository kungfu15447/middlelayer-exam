package com.middlelayer.exam.web
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.IProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RestController
class ProfileController {
    private val profileService: IProfileService
    private val authService: IAuthService

    @Autowired
    constructor(profileService: IProfileService, authService: IAuthService) {
        this.profileService = profileService
        this.authService = authService
    }

    @GetMapping("user/{userid}/profile")
    fun getProfile(@RequestHeader("Authorization") authorization: String, @PathVariable userid: String) : ResponseEntity<Any> {
        try {
            val profile = profileService.getProfile(authorization, userid)
            val headers = HttpHeaders()
            headers.add("Authorization", "Bearer ${authService.register(authorization, userid)}")
            return ResponseEntity<Any>(profile, headers,HttpStatus.OK)
        } catch (ex: Exception) {
            return ResponseEntity<Any>(ex.message, HttpStatus.UNAUTHORIZED)
        }

    }

    @GetMapping("user/test")
    fun getTest(): String {
        return "This is a test"
    }
}