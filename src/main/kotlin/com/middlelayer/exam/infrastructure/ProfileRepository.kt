package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.interfaces.infrastructure.IClient
import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ProfileRepository : IProfileRepository {

    private val xsiClient: IClient
    private val xmlParser: XmlParser

    @Autowired
    constructor(xsiClient: IClient, xmlParser: XmlParser) {
        this.xsiClient = xsiClient
        this.xmlParser = xmlParser
    }
    override fun getProfileXsi(basicAuthToken: String, userId: String): Mono<Profile> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/$userId/profile", basicAuthToken)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue<Profile>(it))
        }
    }

    override fun getServicesFromProfileXsi(basicAuthToken: String, userId: String): Mono<List<Service>> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/$userId/services", basicAuthToken)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue<List<Service>>(it))
        }
    }
}