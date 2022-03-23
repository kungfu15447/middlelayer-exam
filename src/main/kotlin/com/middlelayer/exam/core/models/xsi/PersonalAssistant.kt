package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PersonalAssistant(
    val presence: PresenceEnum? = null,
    val enableExpirationTime: Boolean? = null,
    val expirationTime: Date? = null,
    val enableTransferToAttendant: Boolean? = null,
    val attendantNumber: String? = null,
    val ringSplash: Boolean? = null,
    val callToNumberList: CallToNumberList = CallToNumberList(),
    val alertMeFirst: Boolean? = null,
    val numberOfRings: Int? = null
) : XsiModel()

enum class PresenceEnum(@JsonValue val value: String) {
    NONE("None"),
    LUNCH("Lunch"),
    BUSINESS_TRIP("Business Trip"),
    GONE_FOR_THE_DAY("Gone for the Day"),
    MEETING("Meeting"),
    OUT_OF_OFFICE("Out Of Office"),
    TEMPORARILY_OUT("Temporarily Out"),
    TRAINING("Training"),
    UNAVAILABLE("Unavailable"),
    VACATION("Vacation")
}

fun stringToPresenceEnum(value: String): PresenceEnum {
    return when(value.lowercase()) {
        PresenceEnum.NONE.value.lowercase() -> PresenceEnum.NONE
        PresenceEnum.LUNCH.value.lowercase() -> PresenceEnum.LUNCH
        PresenceEnum.BUSINESS_TRIP.value.lowercase() -> PresenceEnum.BUSINESS_TRIP
        PresenceEnum.GONE_FOR_THE_DAY.value.lowercase() -> PresenceEnum.GONE_FOR_THE_DAY
        PresenceEnum.MEETING.value.lowercase() -> PresenceEnum.MEETING
        PresenceEnum.OUT_OF_OFFICE.value.lowercase() -> PresenceEnum.OUT_OF_OFFICE
        PresenceEnum.TEMPORARILY_OUT.value.lowercase() -> PresenceEnum.TEMPORARILY_OUT
        PresenceEnum.TRAINING.value.lowercase() -> PresenceEnum.TRAINING
        PresenceEnum.UNAVAILABLE.value.lowercase() -> PresenceEnum.UNAVAILABLE
        PresenceEnum.VACATION.value.lowercase() -> PresenceEnum.VACATION
        else -> PresenceEnum.NONE
    }
}