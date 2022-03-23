package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.xsi.Contact
import reactor.core.publisher.Mono

interface IContactService {
    fun getEnterpriseContacts(basicAuthToken: String, userId: String, start: Int, contactRetrieveAmount: Int) : Mono<Contact>
    fun sendMessage(contact: Contact)

}