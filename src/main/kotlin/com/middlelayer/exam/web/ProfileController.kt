package com.middlelayer.exam.web
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.web.bind.annotation.*
import java.net.URL
import com.fasterxml.jackson.databind.node.*
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.middlelayer.exam.core.models.Profile

@RestController
class ProfileController {

    fun getProfileXsi(authorization: String, userid: String) : Profile {
        val client = OkHttpClient()
        val url = URL("https://scalexi-pp-xsp2.tdc.dk/com.broadsoft.xsi-actions/v2.0/user/$userid/profile/")

        val request = Request.Builder()
            .url(url)
            .get()
            .header("Authorization", authorization)
            .build()
        val response = client.newCall(request).execute()

        // Xml String
        val responseBody = response.body!!.string()

        val mapper: XmlMapper = XmlMapper();

        val profile: Profile = mapper.readValue(responseBody)
        return profile;

    }

    @GetMapping("{userid}/profile")
    fun getProfile(@RequestHeader("Authorization") authorization: String, @PathVariable userid: String) : Profile {
        var profile: Profile = getProfileXsi(authorization, userid);
        return profile;
    }
}