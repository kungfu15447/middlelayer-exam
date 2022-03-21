package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.ims.*
import com.middlelayer.exam.core.models.xsi.*
import java.util.*

data class UserSettings(
        var personalAssistant: PersonalAssistantSettings,
        var remoteOffice: RemoteOffice,
        var callForwarding: CallForwardingSettings,
        var numberDisplay: NumberDisplay,
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
            dRemoteOffice: RemoteOffice,
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
            remoteOffice = dRemoteOffice,
            numberDisplay = numberDisplay,
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
        var always: CallForwardingAlways,
        var busy: CallForwardingBusy,
        var noAnswer: CallForwardingNoAnswer
)

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