package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.VoiceMessagingGreeting

data class DVoiceMessagingGreeting(
    var noAnswerNumberOfRings: Int
) {
    constructor(xsiVoiceMessagingGreeting: VoiceMessagingGreeting): this(
        noAnswerNumberOfRings = xsiVoiceMessagingGreeting.noAnswerNumberOfRings ?: 0
    )
}