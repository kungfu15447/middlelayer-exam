package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.RemoteOffice

data class DRemoteOffice(
    var active: Boolean,
    var number: String?
) {
    constructor(xsiRemoteOffice: RemoteOffice) : this(
        active = xsiRemoteOffice.active ?: false,
        number = xsiRemoteOffice.remoteOfficeNumber
    )
}