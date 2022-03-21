package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "CallingLineIDDeliveryBlocking")
data class NumberDisplayHidden(
    val active: Boolean? = null
): XsiModel()