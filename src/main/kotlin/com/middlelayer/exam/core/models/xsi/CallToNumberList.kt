package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class CallToNumberList(
    @JacksonXmlProperty(localName = "callToNumber")
    val callToNumbers: List<CallToNumber> = emptyList()
)