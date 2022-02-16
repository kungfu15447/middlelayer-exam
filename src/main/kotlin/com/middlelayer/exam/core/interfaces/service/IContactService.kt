package com.middlelayer.exam.core.interfaces.service

import com.middlelayer.exam.core.models.domain.DProfile
import com.middlelayer.exam.core.models.xsi.Enterprise
import reactor.core.publisher.Mono

interface IContactService {
    fun getEnterpriseContacts(basicAuthToken: String, userId: String) : Mono<Enterprise>

}