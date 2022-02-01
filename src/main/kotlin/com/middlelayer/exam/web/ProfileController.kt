package com.middlelayer.exam.web
import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.models.Profile
import com.middlelayer.exam.infrastructure.ProfileRepository
import org.springframework.web.bind.annotation.*
import com.middlelayer.exam.service.AuthService
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@RestController
class ProfileController {

    private val auth: AuthService = AuthService()
    private val profileRepository: IProfileRepository = ProfileRepository()

    @GetMapping("user/{userid}/profile")
    fun getProfile(@RequestHeader("Authorization") authorization: String, @PathVariable userid: String) : ResponseEntity<Profile> {

        val profile = profileRepository.getProfileXsi(authorization, userid)
        val headers = HttpHeaders()
        headers.add("Authorization", "Bearer ${auth.register(authorization, userid)}")
        var response = ResponseEntity<Profile>(profile, headers,HttpStatus.OK)

        return response
    }

    @GetMapping("user/test")
    fun getTest(): String {
        return "This is a test"
    }
}