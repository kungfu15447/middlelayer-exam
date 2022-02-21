package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.IContactRepository
import com.middlelayer.exam.core.interfaces.infrastructure.IXsiClient
import com.middlelayer.exam.core.models.xsi.Enterprise
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ContactRepository : IContactRepository {

    private val xsiClient: IXsiClient
    private val xmlParser: XmlParser

    @Autowired
    constructor(xsiClient: IXsiClient, xmlParser: XmlParser) {
        this.xsiClient = xsiClient
        this.xmlParser = xmlParser
    }


    override fun getEnterpriseContacts(basicAuthToken: String, userId: String) : Mono<Enterprise> {
        val responseBody = xsiClient.get("/com.broadsoft.xsi-actions/v2.0/user/m29289547@vk103741.hvoip.dk/directories/Enterprise?sortColumn=firstName&userId=*hvoip.dk*&start=1&results=10", basicAuthToken)
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue<Enterprise>(it))
        }
    }
}