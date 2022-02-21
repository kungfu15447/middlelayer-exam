package com.middlelayer.exam.service

import com.middlelayer.exam.core.interfaces.infrastructure.IContactRepository
import com.middlelayer.exam.core.interfaces.service.IContactService
import com.middlelayer.exam.core.models.domain.DEnterprise
import com.middlelayer.exam.core.models.domain.DService
import com.middlelayer.exam.core.models.xsi.Enterprise
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ContactService : IContactService{


    private val contactRepository: IContactRepository

    @Autowired
    constructor(contactRepository: IContactRepository) {
        this.contactRepository = contactRepository
    }
    override fun getEnterpriseContacts(basicAuthToken: String, userId: String): Mono<Enterprise> {
        return contactRepository.getEnterpriseContacts(basicAuthToken, userId).flatMap {
            Mono.just(it)
        }
    }
}