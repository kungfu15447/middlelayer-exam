package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CallToNumber(
    val type: CallToNumberEnum? = null,
    val alternateNumberId: Int? = null,
)

enum class CallToNumberEnum(@JsonValue val value: String) {
    PRIMARY("Primary"),
    ALTERNATE("Alternate")
}

fun stringToCallToNumberEnum(value: String): CallToNumberEnum {
    return when (value.lowercase()) {
        CallToNumberEnum.PRIMARY.value.lowercase() -> CallToNumberEnum.PRIMARY
        CallToNumberEnum.ALTERNATE.value.lowercase() -> CallToNumberEnum.ALTERNATE
        else -> CallToNumberEnum.PRIMARY
    }
}