package com.middlelayer.exam.infrastructure

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.middlelayer.exam.core.exceptions.InvalidMapException
import org.springframework.stereotype.Component

@Component()
class ObjectParser {
    val xmlMapper: XmlMapper = XmlMapper()
    val jsonMapper: JsonMapper = JsonMapper()

    final inline fun <reified T>tryMapXml(response: String?): T {
        response?.let {
            return xmlMapper.readValue(it)
        }
        throw InvalidMapException("No body to map was returned")
    }

    final inline fun <reified T>tryMapJson(json: String?): T {
        json?.let {
            return jsonMapper.readValue(it)
        }
        throw InvalidMapException("Could not parse JSON to object since JSON string was null")
    }
}