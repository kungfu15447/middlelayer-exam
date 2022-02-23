package com.middlelayer.exam.web.dto.settings

import com.middlelayer.exam.core.models.domain.*
import java.util.*

data class GetSettingsResponseDTO (
    var personalAssistant: PersonalAssistant,
    var remoteOffice: DRemoteOffice,
    var callForwarding: CallForwarding,
    var numberDisplay: DNumberDisplay,
    var voicemail: Voicemail,
    var doNotDisturb: Boolean,
    var blocked: Boolean,
) {
    constructor(
        dPersonalAssistant: DPersonalAssistant,
        assignedNumbers: List<DCallToNumber>,
        availableNumbers: List<DCallToNumber>,
        exclusionNumbers: List<DExclusionNumber>,
        dRemoteOffice: DRemoteOffice,
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
        personalAssistant = PersonalAssistant(
            dPersonalAssistant,
            availableNumbers,
            assignedNumbers,
            exclusionNumbers
        ),
        remoteOffice = dRemoteOffice,
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

data class PersonalAssistant (
    var presence: String,
    var expiration: Date?,
    var transferNumber: String?,
    var transferCalls: Boolean,
    var transferNotification: Boolean,
    var assignedNumbers: List<String>,
    var availableNumbers: List<String>,
    var exclusionNumbers: List<String>
) {
    constructor(
        personalAssistant: DPersonalAssistant,
        dAvailableNumbers: List<DCallToNumber>,
        dAssignedNumbers: List<DCallToNumber>,
        dExclusionNumbers: List<DExclusionNumber>
    ) : this(
        presence = personalAssistant.presence,
        expiration = personalAssistant.expirationTime,
        transferNumber = personalAssistant.attendantNumber,
        transferCalls = personalAssistant.ringSplash,
        transferNotification = personalAssistant.enableTransferToAttendant,
        assignedNumbers = emptyList(),
        availableNumbers = emptyList(),
        exclusionNumbers = emptyList()
    ) {
        assignedNumbers = dAssignedNumbers.map { it.type }
        availableNumbers = dAvailableNumbers.map { it.type }
        exclusionNumbers = dExclusionNumbers.map { it.number }
    }
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
