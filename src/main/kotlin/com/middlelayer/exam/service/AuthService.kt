package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.models.Profile
import com.middlelayer.exam.core.models.Service as ServiceModel
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService : IAuthService {

    @Value("\${jwt.secret.key}")
    private val secretKey: String = ""

    override fun register(basicAuthToken: String, profile: Profile, services: List<ServiceModel>): String {
        val availableServices: List<ServiceModel> = services.filter { it.uri != null }
        return Jwts.builder()
            .setIssuer(profile.details.userId)
            .setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .claim("basicToken", basicAuthToken)
            .claim("services", availableServices)
            .compact();
    }
}