package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class AvailableCallToNumbers(
    @JacksonXmlProperty(localName = "callToNumber")
    val callToNumbers: List<CallToNumber> = emptyList()
)