package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.interfaces.infrastructure.IContactRepository
import com.middlelayer.exam.core.interfaces.infrastructure.IXsiClient
import com.middlelayer.exam.core.models.xsi.Contact
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


    override fun getEnterpriseContacts(basicAuthToken: String, userId: String, start: Int, contactRetrieveAmount: Int) : Mono<Contact> {
        val responseBody = xsiClient.get(
            "/com.broadsoft.xsi-actions/v2.0/user/$userId/directories/Enterprise?sortColumn=firstName&userId=*hvoip.dk*&start=$start&results=$contactRetrieveAmount",
            basicAuthToken
        )
        return responseBody.flatMap {
            Mono.just(xmlParser.tryMapValue<Contact>(it))
        }
    }
}