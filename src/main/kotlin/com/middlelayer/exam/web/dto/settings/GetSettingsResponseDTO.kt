package com.middlelayer.exam.web.dto.settings

import com.middlelayer.exam.core.models.domain.*

data class GetSettingsResponseDTO (
    val voicemail: Voicemail,
    val doNotDisturb: Boolean
) {
    constructor(
        voiceMessaging: DVoiceMessaging,
        voiceMessagingGreeting: DVoiceMessagingGreeting,
        pushNotification: DPushNotification,
        doNotDisturb: DDoNotDisturb
    ) : this(
        voicemail = Voicemail(
            voiceMessaging,
            voiceMessagingGreeting,
            pushNotification
        ),
        doNotDisturb = false,
    )
}

data class Voicemail(
    var active: Boolean,
    var alwaysRedirectToVoiceMail: Boolean,
    var busyRedirectToVoiceMail: Boolean,
    var noAnswerRedirectToVoiceMail: Boolean,
    var noAnswerNumberOfRings: Int,
    var sendPushNotification: Boolean,
) {
    constructor(
        voiceMessaging: DVoiceMessaging,
        voiceMessagingGreeting: DVoiceMessagingGreeting,
        pushNotification: DPushNotification
    ) : this(
        active = voiceMessaging.active,
        alwaysRedirectToVoiceMail = voiceMessaging.alwaysRedirectToVoiceMail,
        busyRedirectToVoiceMail = voiceMessaging.busyRedirectToVoiceMail,
        noAnswerRedirectToVoiceMail = voiceMessaging.noAnswerRedirectToVoiceMail,
        noAnswerNumberOfRings = voiceMessagingGreeting.noAnswerNumberOfRings,
        sendPushNotification = pushNotification.active
    )
}
