package com.middlelayer.exam.core.interfaces.infrastructure

import com.middlelayer.exam.core.models.xsi.Enterprise
import reactor.core.publisher.Mono

interface IContactRepository {
    fun getEnterpriseContacts(basicAuthToken: String, userId: String): Mono<Enterprise>
}