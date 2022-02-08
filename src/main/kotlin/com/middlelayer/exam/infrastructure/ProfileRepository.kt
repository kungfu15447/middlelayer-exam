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
import java.net.URL

@Component
class ProfileRepository : IProfileRepository {

    private val xsiClient: IXsiClient

    @Autowired
    constructor(xsiClient: IXsiClient) {
        this.xsiClient = xsiClient
    }
    override fun getProfileXsi(basicAuthToken: String, userId: String): Profile {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/$userId/profile", basicAuthToken)
        return tryMapValue(responseBody)
    }

    override fun getServicesFromProfileXsi(basicAuthToken: String, userId: String): List<Service> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/$userId/services", basicAuthToken)
        return tryMapValue(responseBody)
    }

    private inline fun <reified T>tryMapValue(response: String?): T {
        response?.let {
            val mapper = XmlMapper()
            return mapper.readValue<T>(it)
        }
        throw Exception("No body to map was returned!")
    }
}