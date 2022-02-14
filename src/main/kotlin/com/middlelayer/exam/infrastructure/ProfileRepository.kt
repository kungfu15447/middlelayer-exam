package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.IProfileRepository
import com.middlelayer.exam.core.interfaces.infrastructure.IXsiClient
import com.middlelayer.exam.core.models.xsi.Profile
import com.middlelayer.exam.core.models.xsi.Service
import com.middlelayer.exam.security.AuthTokenHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ProfileRepository : IProfileRepository {

    private val xsiClient: IXsiClient
    private val xmlParser: XmlParser
    private val tokenHandler: AuthTokenHandler

    @Autowired
    constructor(xsiClient: IXsiClient, xmlParser: XmlParser, tokenHandler: AuthTokenHandler) {
        this.xsiClient = xsiClient
        this.xmlParser = xmlParser
        this.tokenHandler = tokenHandler
    }
    override fun getProfileXsi(userId: String): Mono<Profile> {
        val token = tokenHandler.getBasicToken()
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/$userId/profile", token)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue<Profile>(it))
        }
    }

    override fun getServicesFromProfileXsi(userId: String): Mono<List<Service>> {
        val token = tokenHandler.getBasicToken()
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/$userId/services", token)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue<List<Service>>(it))
        }
    }
}