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
            .compact();
    }
}