package com.middlelayer.exam.infrastructure

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.interfaces.infrastructure.IXsiClient
import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ProfileRepository : IProfileRepository {

    private val xsiClient: IXsiClient

    @Autowired
    constructor(xsiClient: IXsiClient) {
        this.xsiClient = xsiClient
    }
    override fun getProfileXsi(basicAuthToken: String, userId: String): Mono<Profile> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/$userId/profile", basicAuthToken)
        return responseBody.flatMap {
            Mono.just(tryMapValue<Profile>(it))
        }
    }

    override fun getServicesFromProfileXsi(basicAuthToken: String, userId: String): Mono<List<Service>> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/$userId/services", basicAuthToken)
        return responseBody.flatMap {
            Mono.just(tryMapValue<List<Service>>(it))
        }
    }

    private inline fun <reified T>tryMapValue(response: String?): T {
        response?.let {
            val mapper = XmlMapper()
            return mapper.readValue<T>(it)
        }
        throw Exception("No body to map was returned!")
    }
}