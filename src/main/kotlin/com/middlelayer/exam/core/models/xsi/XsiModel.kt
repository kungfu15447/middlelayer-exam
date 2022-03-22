package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

abstract class XsiModel {
    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    @get:JsonIgnore()
    var xmlns = "http://schema.broadsoft.com/xsi"
}