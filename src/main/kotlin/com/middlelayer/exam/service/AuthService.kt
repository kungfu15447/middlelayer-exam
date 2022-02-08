package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.service.IAuthService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService : IAuthService {

    @Value("\${jwt.secret.key}")
    private val secretKey: String = ""

    override fun register(authorization: String, userid: String): String {
        return Jwts.builder()
            .setIssuer(userid)
            .setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .claim("authorization",authorization)
            .compact();
    }
}