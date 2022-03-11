package com.middlelayer.exam.web.dto.settings

data class PutSimultaneousCallDTO(
    val active: Boolean,
    val doNotRingIfOnCall: Boolean,
    val simRingLocations: List<PutSimRingLocation>?
)

data class PutSimRingLocation(
    val address: String,
    val answerConfirmedRequired: Boolean,
)