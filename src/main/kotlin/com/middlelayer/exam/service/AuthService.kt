package com.middlelayer.exam.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.models.auth.ProfileTokenObject
import com.middlelayer.exam.core.models.auth.TokenClaimsObject
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
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .claim("basicToken", basicAuthToken)
            .claim("services", servicesString)
            .claim("profileInfo", profileClaim)
            .compact()
    }

    override fun createBasicAuthToken(user: String, password: String): String {
        val auth = "${user}:${password}"
        return "Basic ${Base64.getEncoder().encodeToString(auth.toByteArray())}"
    }

    override fun getClaimsFromJWTToken(token: String): TokenClaimsObject {
        try {
            val mapper = ObjectMapper()
            val trueToken = token.replace("Bearer", "")
            val claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(trueToken).body
            val basicToken = claims["basicToken"] as String
            val services = claims["services"] as List<String>
            val profile = mapper.convertValue(claims["profileInfo"], ProfileTokenObject::class.java)
            return TokenClaimsObject(services, profile, basicToken)
        } catch(ex: Exception) {
            throw ex
        }
    }
}