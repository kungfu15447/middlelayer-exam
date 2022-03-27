package com.middlelayer.exam.core.models.ims

import com.fasterxml.jackson.annotation.JsonProperty

data class NumberDisplay(
    val userId: String? = null,
    val presentationStatus: PresentationStatusEnum? = null,
    val presentationNumber: String? = null,
    val numberDescription: String? = null
)

enum class PresentationStatusEnum(val value: String) {
    @JsonProperty("Business")
    BUSINESS("Business"),
    @JsonProperty("Mobile")
    MOBILE("Mobile")
}

fun stringToPresentationStatusEnum(value: String): PresentationStatusEnum {
    return when(value.lowercase()) {
        "mobile" -> PresentationStatusEnum.MOBILE
        "business" -> PresentationStatusEnum.BUSINESS
        else -> PresentationStatusEnum.MOBILE
    }
}