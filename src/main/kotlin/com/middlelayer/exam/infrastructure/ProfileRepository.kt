package com.middlelayer.exam.infrastructure

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.models.Profile
import com.middlelayer.exam.core.models.Service
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component
import java.net.URL

@Component
class ProfileRepository : IProfileRepository {

    override fun getProfileXsi(basicAuthToken: String, userId: String): Profile {
        val client = OkHttpClient()
        val url = URL("https://scalexi-pp-xsp2.tdc.dk/com.broadsoft.xsi-actions/v2.0/user/$userId/profile/")
        val request = Request.Builder()
            .url(url)
            .get()
            .header("Authorization", basicAuthToken)
            .build()
        val response = client.newCall(request).execute()

        if (response.code == 200) {
            val responseBody = response.body!!.string()
            val mapper = XmlMapper()
            return mapper.readValue(responseBody)
        } else {
            throw Exception("Unauthorized access")
        }
    }

    override fun getServicesFromProfileXsi(basicAuthToken: String, userId: String): List<Service> {
        val client = OkHttpClient()
        val url = URL("https://scalexi-pp-xsp2.tdc.dk/com.broadsoft.xsi-actions/v2.0/user/$userId/services")
        val request = Request.Builder()
            .url(url)
            .get()
            .header("Authorization", basicAuthToken)
            .build()
        val response = client.newCall(request).execute()

        if (response.code == 200) {
            val responseBody = response.body!!.string()
            val mapper = XmlMapper()
            return mapper.readValue(responseBody)
        } else {
            throw Exception("Unauthorized access")
        }
    }
}