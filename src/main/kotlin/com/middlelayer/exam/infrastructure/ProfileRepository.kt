package com.middlelayer.exam.infrastructure

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.models.Profile
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class ProfileRepository : IProfileRepository {

    override fun getProfileXsi(authorization: String, userid: String): Profile {
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
}