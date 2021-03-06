package com.middlelayer.exam.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.middlelayer.exam.core.exceptions.UnauthorizedException
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.models.auth.BasicTokenObject
import com.middlelayer.exam.core.models.auth.ProfileTokenObject
import com.middlelayer.exam.core.models.auth.TokenClaimsObject
import com.middlelayer.exam.core.models.xsi.Profile
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import java.nio.charset.StandardCharsets
import java.util.*
import com.middlelayer.exam.core.models.xsi.Service as xsiService

@Service
class AuthService : IAuthService {

    @Value("\${jwt.secret.key}")
    private val secretKey: String = ""

    @Value("\${jwt.issuer}")
    private val issuer: String = ""

    override fun register(basicAuthToken: String, profile: Profile, services: List<xsiService>): String {
        val profileClaim = ProfileTokenObject(
            profile.details.userId ?: "",
            profile.details.groupId ?: "",
            "${profile.details.firstName} ${profile.details.lastName}",
            profile.additionalDetails.emailAddress ?: ""
        )

        val servicesString = services.mapNotNull {
            it.name
        }

        return Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .claim("basicToken", basicAuthToken)
            .claim("services", servicesString)
            .claim("profileInfo", profileClaim)
            .compact()
    }

    override fun createBasicAuthToken(user: String, password: String): String {
        val formattedUserName = formatUsername(user)
        val auth = "${formattedUserName}:${password}"
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

    override fun getCredentialsFromBasicToken(token: String): BasicTokenObject {
        try {
            val base64Credentials: String = token.substring(6)
            val credDecoded = Base64.getDecoder().decode(base64Credentials)
            val credentials = String(credDecoded, StandardCharsets.UTF_8)
            val values = credentials.split(":")
            return BasicTokenObject(
                    username = values[0],
                    password = values[1]
            )
        } catch(ex: Exception) {
            throw UnauthorizedException("Could not decode basic token")
        }
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