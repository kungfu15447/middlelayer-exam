package com.middlelayer.exam.web.dto.settings

import com.middlelayer.exam.core.models.xsi.ExclusionNumber
import com.middlelayer.exam.core.models.xsi.PersonalAssistant

data class GetSettingsResponseDTO (
    val personalAssistant: PersonalAssistant = PersonalAssistant(),
    val exclusionNumbers: List<ExclusionNumber> = emptyList()
)
