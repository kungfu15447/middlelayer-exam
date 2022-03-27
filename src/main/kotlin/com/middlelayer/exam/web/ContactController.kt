package com.middlelayer.exam.web

import com.google.gson.Gson
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.IContactService
import com.middlelayer.exam.core.models.xsi.Contact
import com.middlelayer.exam.web.socket.ContactHandler
import okhttp3.internal.wait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.Disposable
import reactor.core.publisher.Mono
import kotlin.math.floor


@RestController
class ContactController {
    private val contactService: IContactService
    private val authService: IAuthService
    private val contactHandler: ContactHandler



    @Autowired
    constructor(contactService: IContactService, authService: IAuthService, contactHandler: ContactHandler) {
        this.contactService = contactService
        this.authService = authService
        this.contactHandler = contactHandler
    }

    @GetMapping("/api/user/contact")
    fun getContacts(@RequestHeader("Authorization") token: String): Any {

        val claims = authService.getClaimsFromJWTToken(token)
        val userId = claims.profileObj.userId
        val basicToken = claims.basicToken
        val password = authService.getCredentialsFromBasicToken(basicToken).password

        val host = "32bc2eb4ea0d49d7b647c5b1d263236b.s2.eu.hivemq.cloud"
        val mqttusername = "Testing"
        val mqttpassword = "Mqtt1234"

        val mqttClient = MqttClient.builder()
            .useMqttVersion5()
            .serverHost(host)
            .serverPort(8884)
            .webSocketConfig()
            .serverPath("mqtt")
            .applyWebSocketConfig()
            .sslWithDefaultConfig()
            .buildBlocking()

        mqttClient.connectWith()
            .simpleAuth()
            .username(mqttusername)
            .password(Charsets.UTF_8.encode(mqttpassword))
            .applySimpleAuth()
            .send();

        getContact(userId, password, mqttClient)

        return HttpStatus.OK
    }

    fun getContact(userId: String, password: String, mqttClient: Mqtt5BlockingClient): Disposable {
        val contactRetrieveAmount = 100
        return contactService.getEnterpriseContacts(authService.createBasicAuthToken(userId, password), userId, start = 1, contactRetrieveAmount).subscribe {
            var totalAvailableRecords = it.totalAvailableRecords
            publishToMqtt(userId, it, mqttClient)

            val totalRecursions = floor((totalAvailableRecords/100).toDouble())
            var i = 1
            while(totalRecursions >= i) {
                kotlin.run {
                    contactService.getEnterpriseContacts(authService.createBasicAuthToken(userId, password),userId, (i * 100) + 1, contactRetrieveAmount).subscribe { nextIt ->
                        totalAvailableRecords = nextIt.totalAvailableRecords
                        publishToMqtt(userId, nextIt, mqttClient)
                        }
                    }
                i++
            }

        }
    }
    fun publishToMqtt(userId: String, payload: Contact, mqttClient: Mqtt5BlockingClient ) {
        val gson = Gson()
        mqttClient.publishWith()
            .topic("contacts/$userId")
            .payload(Charsets.UTF_8.encode(gson.toJson(payload)))
            .send();
    }
}