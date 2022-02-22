package com.middlelayer.exam.core.models.xsi

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

data class VoiceMessagingGreeting(
    val busyAnnouncementSelection: String? = null,
    val noAnswerAnnouncementSelection: String? = null,
    val disableExtendedAwayMessageDeposit: Boolean? = null,
    val enableExtendedAwayGreeting: Boolean? = null,
    val noAnswerNumberOfRings: Int? = null,
    val disableMessageDeposit: Boolean? = null,
    val disableMessageDepositAction: String? = null,
    val greetingOnlyForwardDestination: String? = null,
    @JacksonXmlElementWrapper(useWrapping = false)
    val busyPersonalAudioFile: List<BusyPersonalAudioFile> = emptyList(),
    @JacksonXmlElementWrapper(useWrapping = false)
    val noAnswerPersonalAudioFile: List<NoAnswerPersonalAudioFile> = emptyList()
)

data class BusyPersonalAudioFile(
    val description: String? = null
)

data class NoAnswerPersonalAudioFile(
    val description: String? = null
)