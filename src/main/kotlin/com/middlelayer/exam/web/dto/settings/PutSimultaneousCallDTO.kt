package com.middlelayer.exam.web.dto.settings

data class PutSimultaneousCallDTO(
    val active: Boolean,
    val incomingCalls: String,
    val simRingLocations: List<PutSimRingLocation> = emptyList()
)

data class PutSimRingLocation(
    val address: String,
    val answerConfirmedRequired: Boolean,
)