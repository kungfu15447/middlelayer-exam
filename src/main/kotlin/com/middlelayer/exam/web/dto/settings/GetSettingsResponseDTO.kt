package com.middlelayer.exam.web.dto.settings

import com.middlelayer.exam.core.models.domain.*

data class GetSettingsResponseDTO (
    var callForwarding: CallForwarding,
    var numberDisplay: DNumberDisplay,
    var voicemail: Voicemail,
    var doNotDisturb: Boolean,
    var blocked: Boolean,
) {
    constructor(
        callForwardingAlways: DCallForwardingAlways,
        callForwardingBusy: DCallForwardingBusy,
        callForwardingNoAnswer: DCallForwardingNoAnswer,
        numberDisplayHidden: DNumberDisplayHidden,
        numberDisplay: DNumberDisplay,
        voiceMessaging: DVoiceMessaging,
        voiceMessagingGreeting: DVoiceMessagingGreeting,
        pushNotification: DPushNotification,
        doNotDisturb: DDoNotDisturb
    ) : this(
        numberDisplay = numberDisplay,
        callForwarding = CallForwarding(
            callForwardingAlways,
            callForwardingBusy,
            callForwardingNoAnswer
        ),
        voicemail = Voicemail(
            voiceMessaging,
            voiceMessagingGreeting,
            pushNotification
        ),
        doNotDisturb = doNotDisturb.active,
        blocked = numberDisplayHidden.active,
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

data class CallForwarding(
    var always: DCallForwardingAlways,
    var busy: DCallForwardingBusy,
    var noAnswer: DCallForwardingNoAnswer
)
