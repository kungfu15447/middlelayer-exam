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
    private val objectParser: ObjectParser

    @Autowired
    constructor(xsiClient: IClient, objectParser: ObjectParser) {
        this.xsiClient = xsiClient
        this.objectParser = objectParser
    }
    override fun getProfileXsi(basicAuthToken: String, userId: String): Mono<Profile> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/$userId/profile", basicAuthToken)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml<Profile>(it))
        }
    }

    override fun getServicesFromProfileXsi(basicAuthToken: String, userId: String): Mono<List<Service>> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/$userId/services", basicAuthToken)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml<List<Service>>(it))
        }
    }
}