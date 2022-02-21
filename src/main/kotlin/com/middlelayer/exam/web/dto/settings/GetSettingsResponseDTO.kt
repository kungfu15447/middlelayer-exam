package com.middlelayer.exam.web.dto.settings

import com.middlelayer.exam.core.models.domain.DCallToNumber
import com.middlelayer.exam.core.models.ims.NumberDisplay
import com.middlelayer.exam.core.models.xsi.ExclusionNumber
import com.middlelayer.exam.core.models.xsi.NumberDisplayHidden
import com.middlelayer.exam.core.models.xsi.PersonalAssistant
import com.middlelayer.exam.core.models.xsi.RemoteOffice

data class GetSettingsResponseDTO (
    val personalAssistant: PersonalAssistant = PersonalAssistant(),
    val exclusionNumbers: List<ExclusionNumber> = emptyList(),
    val assignedCallToNumbers: List<DCallToNumber> = emptyList(),
    val availableCallToNumbers: List<DCallToNumber> = emptyList(),
    val remoteOffice: RemoteOffice = RemoteOffice(),
    val numberDisplayStatus: NumberDisplayHidden = NumberDisplayHidden(),
    val numberDisplay: NumberDisplay = NumberDisplay()
)
