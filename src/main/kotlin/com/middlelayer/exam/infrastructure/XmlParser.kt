package com.middlelayer.exam.infrastructure

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.middlelayer.exam.core.exceptions.InvalidMapException
import org.springframework.stereotype.Component

@Component()
class XmlParser {
    val mapper: XmlMapper = XmlMapper()

    final inline fun <reified T>tryMapValue(response: String?): T {
        response?.let {
            val mapper = XmlMapper()
            return mapper.readValue(it)
        }
        throw InvalidMapException("No body to map was returned")
    }
}