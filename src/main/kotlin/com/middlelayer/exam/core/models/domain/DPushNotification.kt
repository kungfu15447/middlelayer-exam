package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.MWIDeliveryToMobileEndpoint

data class DPushNotification(
    var active: Boolean,
    var phoneNumber: String
) {
    constructor(xsiMWIDeliveryToMobileEndpoint: MWIDeliveryToMobileEndpoint) : this(
        active = xsiMWIDeliveryToMobileEndpoint.active ?: false,
        phoneNumber = xsiMWIDeliveryToMobileEndpoint.mobilePhoneNumber ?: ""
    )
}