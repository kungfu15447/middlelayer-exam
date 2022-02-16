package com.middlelayer.exam.web

import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.IContactService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ContactController {
    private val contactService: IContactService
    private val authService: IAuthService

    @Autowired
    constructor(contactService: IContactService, authService: IAuthService) {
        this.contactService = contactService
        this.authService = authService
    }

    @GetMapping("/api/user/contact")
    fun getContacts(@RequestParam userId : String): Mono<ResponseEntity<Any>> {
        val response = contactService.getEnterpriseContacts(authService.createBasicAuthToken(userId, password = "111111"),userId)
        return Mono.just(ResponseEntity<Any>(response, HttpStatus.OK))

    }

}