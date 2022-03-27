package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("PersonalAssistantExclusionNumber")
data class ExclusionNumber(
    val number: String? = null
): XsiModel() {
    val description: String = "-"
}
