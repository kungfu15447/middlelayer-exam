package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.models.auth.ProfileTokenObject
import com.middlelayer.exam.core.models.domain.DProfile
import com.middlelayer.exam.core.models.domain.DService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService : IAuthService {

    @Value("\${jwt.secret.key}")
    private val secretKey: String = ""

    override fun register(basicAuthToken: String, profile: DProfile, services: List<DService>): String {
        val profileClaim = ProfileTokenObject(
            profile.userId,
            profile.groupId,
            "${profile.firstName} ${profile.lastName}",
            profile.email
        )

        val servicesString = services.map {
            it.name
        }.filterNotNull()

        return Jwts.builder()
            .setIssuer(profile.userId)
            .setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .claim("basicToken", basicAuthToken)
            .claim("services", servicesString)
            .claim("profileInfo", profileClaim)
            .compact()
    }

    override fun createBasicAuthToken(user: String, password: String, withFormat: Boolean): String {
        var auth = ""
        if (withFormat) {
            auth = "${formatUsername(user)}:${password}"
        } else {
            auth = "${user}:${password}"
        }
        return "Basic ${Base64.getEncoder().encodeToString(auth.toByteArray())}"
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