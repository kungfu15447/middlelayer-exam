package com.middlelayer.exam.web

import com.google.gson.Gson
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import com.middlelayer.exam.core.interfaces.service.IAuthService
import com.middlelayer.exam.core.interfaces.service.IContactService
import com.middlelayer.exam.core.models.xsi.Contact
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import reactor.core.Disposable
import kotlin.math.floor


@RestController
class ContactController {
    private val contactService: IContactService
    private val authService: IAuthService


    @Value("\${mqtt.host}")
    private val mqttHost: String = ""

    @Value("\${mqtt.username}")
    private val mqttUsername: String = ""

    @Value("\${mqtt.password}")
    private val mqttPassword: String = ""


    @Autowired
    constructor(contactService: IContactService, authService: IAuthService) {
        this.contactService = contactService
        this.authService = authService
    }

    @GetMapping("/api/user/contact")
    fun getContacts(@RequestHeader("Authorization") token: String): Any {

        val claims = authService.getClaimsFromJWTToken(token)
        val userId = claims.profileObj.userId
        val basicToken = claims.basicToken

        val mqttClient = MqttClient.builder()
            .useMqttVersion5()
            .serverHost(mqttHost)
            .serverPort(8884)
            .webSocketConfig()
            .serverPath("mqtt")
            .applyWebSocketConfig()
            .sslWithDefaultConfig()
            .buildBlocking()

        mqttClient.connectWith()
            .simpleAuth()
            .username(mqttUsername)
            .password(Charsets.UTF_8.encode(mqttPassword))
            .applySimpleAuth()
            .send()

        getContact(userId, basicToken, mqttClient)

        return HttpStatus.OK
    }

    fun getContact(userId: String, token: String, mqttClient: Mqtt5BlockingClient): Disposable {
        val contactRetrieveAmount = 100
        val start = 1
        return contactService.getEnterpriseContacts(token, userId, start, contactRetrieveAmount).subscribe {
            var totalAvailableRecords = it.totalAvailableRecords
            publishToMqtt(userId, it, mqttClient)

            val totalRecursions = floor((totalAvailableRecords / 100).toDouble())
            var i = 1
            while (totalRecursions >= i) {
                contactService.getEnterpriseContacts(token, userId, (i * 100) + 1, contactRetrieveAmount)
                    .subscribe { nextIt ->
                        totalAvailableRecords = nextIt.totalAvailableRecords
                        publishToMqtt(userId, nextIt, mqttClient)
                    }
                i++
            }

        }
    }

    fun publishToMqtt(userId: String, payload: Contact, mqttClient: Mqtt5BlockingClient) {
        val gson = Gson()
        mqttClient.publishWith()
            .topic("contacts/$userId")
            .payload(Charsets.UTF_8.encode(gson.toJson(payload)))
            .send();
    }
}