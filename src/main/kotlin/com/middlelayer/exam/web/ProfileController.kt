package com.middlelayer.exam.web
import com.middlelayer.exam.core.interfaces.service.IProfileService
import org.springframework.web.bind.annotation.*
import com.middlelayer.exam.service.AuthService
import com.middlelayer.exam.service.ProfileService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RestController
class ProfileController {

    private val auth: AuthService = AuthService()
    private val profileService: IProfileService = ProfileService()

    @GetMapping("user/{userid}/profile")
    fun getProfile(@RequestHeader("Authorization") authorization: String, @PathVariable userid: String) : ResponseEntity<Any> {

        try {
            val profile = profileService.getProfile(authorization, userid)
            val headers = HttpHeaders()
            headers.add("Authorization", "Bearer ${auth.register(authorization, userid)}")
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