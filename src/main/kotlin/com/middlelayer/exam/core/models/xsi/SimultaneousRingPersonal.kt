package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class SimultaneousRingPersonal(
    val active: Boolean? = false,
    val incomingCalls: String? = null,
    val criteriaActivationList: CriteriaActivationList = CriteriaActivationList()
)

data class CriteriaActivationList(
    @JacksonXmlProperty(localName = "criteriaActivation")
    val criteriaActivations: List<CriteriaActivation> = emptyList()
)

data class CriteriaActivation(
    val address: String? = null,
    val answerConfirmationRequired: Boolean? = null,
)