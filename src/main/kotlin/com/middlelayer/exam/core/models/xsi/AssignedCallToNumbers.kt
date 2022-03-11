package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("PersonalAssistantAssignedCallToNumbers")
data class AssignedCallToNumbers(
    val callToNumberList: CallToNumberList = CallToNumberList()
): XsiModel()