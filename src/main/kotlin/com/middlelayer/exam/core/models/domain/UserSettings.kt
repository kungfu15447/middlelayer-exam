package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.ims.*
import com.middlelayer.exam.core.models.xsi.*
import java.util.*

data class UserSettings(
        var personalAssistant: PersonalAssistantSettings,
        var remoteOffice: RemoteOfficeSettings,
        var callForwarding: CallForwardingSettings,
        var numberDisplay: NumberDisplaySettings,
        var voicemail: VoicemailSettings,
        var simultaneousCall: SimultaneousCallSettings,
        var doNotDisturb: Boolean,
        var blocked: Boolean,
) {
    constructor(
            personalAssistant: PersonalAssistant,
            assignedNumbers: List<CallToNumber>,
            availableNumbers: List<CallToNumber>,
            exclusionNumbers: List<ExclusionNumber>,
            remoteOffice: RemoteOffice,
            callForwardingAlways: CallForwardingAlways,
            callForwardingBusy: CallForwardingBusy,
            callForwardingNoAnswer: CallForwardingNoAnswer,
            numberDisplayHidden: NumberDisplayHidden,
            numberDisplay: NumberDisplay,
            voiceMessaging: VoiceMessaging,
            voiceMessagingGreeting: VoiceMessagingGreeting,
            pushNotification: MWIDeliveryToMobileEndpoint,
            simultaneousRingPersonal: SimultaneousRingPersonal,
            doNotDisturb: DoNotDisturb
    ) : this(
            personalAssistant = PersonalAssistantSettings(
                    personalAssistant,
                    availableNumbers,
                    assignedNumbers,
                    exclusionNumbers
            ),
            remoteOffice = RemoteOfficeSettings(remoteOffice),
            numberDisplay = NumberDisplaySettings(numberDisplay),
            callForwarding = CallForwardingSettings(
                    callForwardingAlways,
                    callForwardingBusy,
                    callForwardingNoAnswer
            ),
            voicemail = VoicemailSettings(
                    voiceMessaging,
                    voiceMessagingGreeting,
                    pushNotification
            ),
            simultaneousCall = SimultaneousCallSettings(
                    simultaneousRingPersonal
            ),
            doNotDisturb = doNotDisturb.active ?: false,
            blocked = numberDisplayHidden.active ?: false,
    )
}

data class PersonalAssistantSettings(
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
            personalAssistant: PersonalAssistant,
            availableNumbers: List<CallToNumber>,
            assignedNumbers: List<CallToNumber>,
            exclusionNumbers: List<ExclusionNumber>
    ) : this(
            presence = personalAssistant.presence?.value ?: PresenceEnum.NONE.value,
            expiration = personalAssistant.expirationTime,
            transferNumber = personalAssistant.attendantNumber,
            transferCalls = personalAssistant.ringSplash ?: false,
            transferNotification = personalAssistant.enableTransferToAttendant ?: false,
            assignedNumbers = emptyList(),
            availableNumbers = emptyList(),
            exclusionNumbers = emptyList()
    ) {
        this.assignedNumbers = assignedNumbers.map { it.type?.value ?: CallToNumberEnum.PRIMARY.value }
        this.availableNumbers = availableNumbers.map { it.type?.value ?: CallToNumberEnum.PRIMARY.value }
        this.exclusionNumbers = exclusionNumbers.map { it.number ?: "" }
    }
}

data class VoicemailSettings(
        var active: Boolean,
        var alwaysRedirectToVoiceMail: Boolean,
        var busyRedirectToVoiceMail: Boolean,
        var noAnswerRedirectToVoiceMail: Boolean,
        var noAnswerNumberOfRings: Int,
        var sendPushNotification: Boolean,
) {
    constructor(
            voiceMessaging: VoiceMessaging,
            voiceMessagingGreeting: VoiceMessagingGreeting,
            pushNotification: MWIDeliveryToMobileEndpoint
    ) : this(
            active = voiceMessaging.active ?: false,
            alwaysRedirectToVoiceMail = voiceMessaging.alwaysRedirectToVoiceMail ?: false,
            busyRedirectToVoiceMail = voiceMessaging.busyRedirectToVoiceMail ?: false,
            noAnswerRedirectToVoiceMail = voiceMessaging.noAnswerRedirectToVoiceMail ?: false,
            noAnswerNumberOfRings = voiceMessagingGreeting.noAnswerNumberOfRings ?: 0,
            sendPushNotification = pushNotification.active ?: false
    )
}

data class CallForwardingSettings(
        var always: CallForwardingAlwaysSettings,
        var busy: CallForwardingBusySettings,
        var noAnswer: CallForwardingNoAnswerSettings
) {
    constructor(
            xsiAlways: CallForwardingAlways,
            xsiBusy: CallForwardingBusy,
            xsiNoAnswer: CallForwardingNoAnswer
    ) : this(
            always = CallForwardingAlwaysSettings(xsiAlways),
            busy = CallForwardingBusySettings(xsiBusy),
            noAnswer = CallForwardingNoAnswerSettings(xsiNoAnswer)
    )
}

data class CallForwardingAlwaysSettings(
        var active: Boolean,
        var phoneNumber: String?,
) {
    constructor(
            always: CallForwardingAlways
    ) : this(
            active = always.active ?: false,
            phoneNumber = always.forwardToPhoneNumber
    )
}

data class CallForwardingBusySettings(
        var active: Boolean,
        var phoneNumber: String?,
) {
    constructor(
            busy: CallForwardingBusy
    ) : this(
            active = busy.active ?: false,
            phoneNumber = busy.forwardToPhoneNumber
    )
}

data class CallForwardingNoAnswerSettings(
        var active: Boolean,
        var phoneNumber: String?,
        var numberOfRings: Int
) {
    constructor(
            noAnswer: CallForwardingNoAnswer
    ) : this(
            active = noAnswer.active ?: false,
            phoneNumber = noAnswer.forwardToPhoneNumber,
            numberOfRings = noAnswer.numberOfRings ?: 0
    )
}

data class NumberDisplaySettings(
        var type: String,
        var currentNumber: String
) {
    constructor(
            numberDisplay: NumberDisplay
    ) : this(
            type = numberDisplay.presentationStatus?.value ?: PresentationStatusEnum.MOBILE.value,
            currentNumber = numberDisplay.presentationNumber ?: ""
    )
}

data class RemoteOfficeSettings(
        var active: Boolean,
        var number: String?
) {
    constructor(remoteOffice: RemoteOffice) : this(
            active = remoteOffice.active ?: false,
            number = remoteOffice.remoteOfficeNumber
    )
}

data class SimultaneousCallSettings(
        var active: Boolean,
        var doNotRingIfOnCall: Boolean,
        var phoneNumbers: List<PhoneNumber> = emptyList()
) {
    constructor(
            simultaneousRingPersonal: SimultaneousRingPersonal
    ) : this(
            active = simultaneousRingPersonal.active ?: false,
            doNotRingIfOnCall = simultaneousRingPersonal.incomingCalls.let {
                if (it == null) {
                    false
                } else {
                    it == IncomingCallsEnum.DoNotRing
                }
            },
            phoneNumbers = emptyList()
    ) {
        simultaneousRingPersonal.simRingLocations?.let {
            phoneNumbers = it.simRingLocation.map { simRing ->
                PhoneNumber(
                        address = simRing.address ?: "",
                        answerConfirmationRequired = simRing.answerConfirmationRequired ?: false
                )
            }
        }
    }
}

data class PhoneNumber(
        var address: String,
        var answerConfirmationRequired: Boolean
)