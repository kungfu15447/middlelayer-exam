package com.middlelayer.exam

import com.hivemq.client.mqtt.MqttClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExamApplication

fun main(args: Array<String>) {
	runApplication<ExamApplication>(*args)

}

