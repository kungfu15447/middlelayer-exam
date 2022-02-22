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
    val busyPersonalAudioFile: BusyPersonalAudioFile = BusyPersonalAudioFile(),
    val busyPersonalVideoFile: BusyPersonalVideoFile = BusyPersonalVideoFile(),
    val noAnswerPersonalAudioFile: NoAnswerPersonalAudioFile = NoAnswerPersonalAudioFile(),
    val noAnswerPersonalVideoFile: NoAnswerPersonalVideoFile = NoAnswerPersonalVideoFile()
)

data class BusyPersonalAudioFile(
    val description: String? = null
)

data class BusyPersonalVideoFile(
    val description: String? = null,
)

data class NoAnswerPersonalVideoFile(
    val description: String? = null,
)

data class NoAnswerPersonalAudioFile(
    val description: String? = null
)