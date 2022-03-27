package com.middlelayer.exam.web.dto.settings

data class PutCallForwardDTO(
    val always: PutCallForwardAlways?,
    val busy: PutCallForwardBusy?,
    val noAnswer: PutCallForwardNoAnswer?
)

data class PutCallForwardAlways(
    val active: Boolean,
    val phoneNumber: String?,
)

data class PutCallForwardBusy(
    val active: Boolean,
    val phoneNumber: String?,
)

data class PutCallForwardNoAnswer(
    val active: Boolean,
    val phoneNumber: String?,
    val numberOfRings: Int?,
)