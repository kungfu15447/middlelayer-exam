package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.ISettingsRepository
import com.middlelayer.exam.core.interfaces.infrastructure.IClient
import com.middlelayer.exam.core.models.ims.NumberDisplay
import com.middlelayer.exam.core.models.xsi.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class SettingsRepository : ISettingsRepository {
    private val xsiClient: IClient
    private val imsClient: IClient
    private val objectParser: ObjectParser

    @Autowired
    constructor(xsiClient: IClient, imsClient: IClient, objectParser: ObjectParser) {
        this.xsiClient = xsiClient
        this.imsClient = imsClient
        this.objectParser = objectParser
    }

    override fun getPersonalAssistant(token: String, userId: String): Mono<PersonalAssistant> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/personalassistant", token)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml(it))
        }
    }

    override fun getPAExclusionNumbers(token: String, userId: String): Mono<List<ExclusionNumber>> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/personalassistant/exclusionnumberlist", token)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml(it))
        }
    }

    override fun getPAAssignedCallToNumbers(token: String, userId: String): Mono<AssignedCallToNumbers> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/personalassistant/assignedcalltonumbers", token)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml(it))
        }
    }

    override fun getPAAvailableCallToNumbers(token: String, userId: String): Mono<AvailableCallToNumbers> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/personalassistant/availablecalltonumbers", token)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml(it))
        }
    }

    override fun getRemoteOffice(token: String, userId: String): Mono<RemoteOffice> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/remoteoffice", token)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml(it))
        }
    }

    override fun getNumberDisplayStatus(token: String, userId: String): Mono<NumberDisplayHidden> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/CallingLineIDDeliveryBlocking", token)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml(it))
        }
    }

    override fun getNumberDisplay(token: String, userId: String): Mono<NumberDisplay> {
        val responseBody = imsClient.get("/nef/clid/user/${userId}/service", token)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapJson(it))
        }
    }

    override fun getCallForwardingAlways(token: String, userId: String): Mono<CallForwardingAlways> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/callforwardingalways", token)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml(it))
        }
    }

    override fun getCallForwardingNoAnswer(token: String, userId: String): Mono<CallForwardingNoAnswer> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/callforwardingnoanswer", token)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml(it))
        }
    }

    override fun getCallForwardingBusy(token: String, userId: String): Mono<CallForwardingBusy> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/${userId}/services/callforwardingnbusy", token)
        return responseBody.flatMap {
            Mono.just(objectParser.tryMapXml(it))
        }
    }
}