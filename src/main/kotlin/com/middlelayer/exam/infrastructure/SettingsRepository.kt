package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.ISettingsRepository
import com.middlelayer.exam.core.interfaces.infrastructure.IXsiClient
import com.middlelayer.exam.core.models.xsi.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class SettingsRepository : ISettingsRepository {
    private val xsiClient: IXsiClient
    private val xmlParser: XmlParser

    @Autowired
    constructor(xsiClient: IXsiClient, xmlParser: XmlParser) {
        this.xsiClient = xsiClient
        this.xmlParser = xmlParser
    }

    override fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/personalassistant", token)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue<PersonalAssistant>(it))
        }
    }

    override fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/personalassistant/exclusionnumberlist", token)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue<List<ExclusionNumber>>(it))
        }
    }

    override fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<PersonalAssistantAssignedCallToNumbers> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/personalassistant/assignedcalltonumbers", token)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue<PersonalAssistantAssignedCallToNumbers>(it))
        }
    }


}