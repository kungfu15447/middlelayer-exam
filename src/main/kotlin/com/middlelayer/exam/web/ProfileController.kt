package com.middlelayer.exam.web
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.web.bind.annotation.*
import java.net.URL
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.middlelayer.exam.core.models.Profile
import com.middlelayer.exam.service.AuthService
import io.jsonwebtoken.Jwts
import org.springframework.http.ResponseEntity

@RestController
class ProfileController {

    private val auth: AuthService = AuthService();

    fun getProfileXsi(authorization: String, userid: String): Profile {
        val client = OkHttpClient()
        val url = URL("https://scalexi-pp-xsp2.tdc.dk/com.broadsoft.xsi-actions/v2.0/user/$userid/profile/")

        val request = Request.Builder()
            .url(url)
            .get()
            .header("Authorization", authorization)
            .build()
        val response = client.newCall(request).execute()

        val responseBody = response.body!!.string()

        val mapper = XmlMapper();

        return mapper.readValue(responseBody);

    }
    
    @GetMapping("user/{userid}/profile")
    fun getProfile(@RequestHeader("jwt") jwt: String?,@RequestHeader("Authorization") authorization: String, @PathVariable userid: String) : ResponseEntity<Any> {

        if(jwt.isNullOrEmpty()) {
            val jwtToken = auth.register(authorization,userid)
            return ResponseEntity.status(401).body(jwtToken);
        }

        val jwtBody = Jwts.parser().setSigningKey("cfd0209b-7a93-482f-97d0-cd9d368a5533").parseClaimsJws(jwt).body
        val jwtAuthorization: String = jwtBody["authorization"].toString()

        return ResponseEntity.ok(getProfileXsi(jwtAuthorization,userid))
    }
}