package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.service.IAuthService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import javax.crypto.SecretKey

@Service
class AuthService : IAuthService {
    override fun register(authorization: String, userid: String): String {
        return Jwts.builder()
            .setIssuer(userid)
            .setExpiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            .signWith(SignatureAlgorithm.HS256, "cfd0209b-7a93-482f-97d0-cd9d368a5533")
            .claim("authorization",authorization)
            .compact();
    }
}