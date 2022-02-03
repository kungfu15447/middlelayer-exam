package com.middlelayer.exam.infrastructure

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.interfaces.infrastructure.IXsiClient
import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.net.URL

@Component
class ProfileRepository : IProfileRepository {

    private val xsiClient: IXsiClient

    @Autowired
    constructor(xsiClient: IXsiClient) {
        this.xsiClient = xsiClient
    }
    override fun getProfileXsi(basicAuthToken: String, userId: String): Profile {
        val responseBody = xsiClient.get("com.broadsoft.xsi-actions/v2.0/user/$userId/services", basicAuthToken)
        val mapper = XmlMapper()
        return mapper.readValue(responseBody)
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