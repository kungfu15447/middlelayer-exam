package com.middlelayer.exam.core.interfaces.infrastructure

import com.middlelayer.exam.core.models.xsi.Contact
import reactor.core.publisher.Mono

interface IContactRepository {
    fun getEnterpriseContacts(basicAuthToken: String, userId: String, start: Int, contactRetrieveAmount: Int): Mono<Contact>
}