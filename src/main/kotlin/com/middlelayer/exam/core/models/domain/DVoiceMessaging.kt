package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.VoiceMessaging

data class DVoiceMessaging(
    var active: Boolean,
    var alwaysRedirectToVoiceMail: Boolean,
    var busyRedirectToVoiceMail: Boolean,
    var noAnswerRedirectToVoiceMail: Boolean
) {
    constructor(xsiVoiceMessaging: VoiceMessaging) : this(
        active = xsiVoiceMessaging.active ?: false,
        alwaysRedirectToVoiceMail = xsiVoiceMessaging.alwaysRedirectToVoiceMail ?: false,
        busyRedirectToVoiceMail = xsiVoiceMessaging.busyRedirectToVoiceMail ?: false,
        noAnswerRedirectToVoiceMail = xsiVoiceMessaging.noAnswerRedirectToVoiceMail ?: false,
    )
}