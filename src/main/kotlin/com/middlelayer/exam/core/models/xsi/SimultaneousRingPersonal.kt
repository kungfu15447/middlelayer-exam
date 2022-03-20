package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
data class SimultaneousRingPersonal(
    val active: Boolean? = false,
    val incomingCalls: IncomingCallsEnum? = null,
    val simRingLocations: SimRingLocations? = null,
    //Unknown property that just need to be there for xml parsing purposes
    val criteriaActivationList: Any? = null
): XsiModel()

data class SimRingLocations(
    val simRingLocation: List<SimRingLocation> = emptyList()
)

data class SimRingLocation(
    val address: String? = null,
    val answerConfirmationRequired: Boolean? = null,
)

enum class IncomingCallsEnum {
    @JsonProperty("Do not Ring if on a Call")
    DoNotRing,
    @JsonProperty("Ring for all Incoming Calls")
    RingForAll
}

fun stringToIncomingCallsEnum(incomingCallString: String): IncomingCallsEnum {
    return when (incomingCallString) {
        "Do not Ring if on  a Call" -> IncomingCallsEnum.DoNotRing
        "Ring for all Incoming Calls" -> IncomingCallsEnum.RingForAll
        else -> IncomingCallsEnum.RingForAll
    }
}