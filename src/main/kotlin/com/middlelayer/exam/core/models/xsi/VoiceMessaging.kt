package com.middlelayer.exam.core.models.xsi

data class VoiceMessaging(
    val active: Boolean? = null,
    val processing: String? = null,
    val voiceMessageDeliveryEmailAddress: String? = null,
    val usePhoneMessageWaitingIndicator: Boolean? = null,
    val sendVoiceMessageNotifyEmail: Boolean? = null,
    val voiceMessageNotifyEmailAddress: String? = null,
    val sendCarbonCopyVoiceMessage: Boolean? = null,
    val voiceMessageCarbonCopyEmailAddress: String? = null,
    val transferOnZeroToPhoneNumber: Boolean? = null,
    val transferPhoneNumber: String? = null,
    val alwaysRedirectToVoiceMail: Boolean? = null,
    val busyRedirectToVoiceMail: Boolean? = null,
    val noAnswerRedirectToVoiceMail: Boolean? = null,
    val outOfPrimaryZoneRedirectToVoiceMail: Boolean? = null,
)