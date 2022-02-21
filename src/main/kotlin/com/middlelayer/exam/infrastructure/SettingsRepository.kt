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
            Mono.just(xmlParser.tryMapValue(it))
        }
    }

    override fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/personalassistant/exclusionnumberlist", token)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue(it))
        }
    }

    override fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<AssignedCallToNumbers> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/personalassistant/assignedcalltonumbers", token)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue(it))
        }
    }

    override fun getPAAvailableCallToNumbers(token: String, userId: String): Mono<AvailableCallToNumbers> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/personalassistant/availablecalltonumbers", token)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue(it))
        }
    }

    override fun getRemoteOffice(token: String, userId: String): Mono<RemoteOffice> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/remoteoffice", token)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue(it))
        }
    }

    override fun getNumberDisplayStatus(token: String, userId: String): Mono<NumberDisplayHidden> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/CallingLineIDDeliveryBlocking", token)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue(it))
        }
    }
}