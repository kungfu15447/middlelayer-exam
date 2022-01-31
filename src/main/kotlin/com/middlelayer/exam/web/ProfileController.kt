package com.middlelayer.exam.web
import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.infrastructure.ProfileRepository
import org.springframework.web.bind.annotation.*
import com.middlelayer.exam.service.AuthService
import io.jsonwebtoken.Jwts
import org.springframework.http.ResponseEntity

@RestController
class ProfileController {

    private val auth: AuthService = AuthService()
    private val profileRepository: IProfileRepository = ProfileRepository()

    @GetMapping("user/{userid}/profile")
    fun getProfile(@RequestHeader("jwt") jwt: String?,@RequestHeader("Authorization") authorization: String, @PathVariable userid: String) : ResponseEntity<Any> {

        if(jwt.isNullOrEmpty()) {
            val jwtToken = auth.register(authorization,userid)
            return ResponseEntity.status(401).body(jwtToken);
        }

        val jwtBody = Jwts.parser().setSigningKey("cfd0209b-7a93-482f-97d0-cd9d368a5533").parseClaimsJws(jwt).body
        val jwtAuthorization: String = jwtBody["authorization"].toString()

        return ResponseEntity.ok(profileRepository.getProfileXsi(jwtAuthorization,userid))
    }

    @GetMapping("user/test")
    fun getTest(): String {
        return "This is a test"
    }
}