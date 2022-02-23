package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.NumberDisplayHidden

data class DNumberDisplayHidden(
    var active: Boolean
) {
    constructor(xsiNumberDisplayHidden: NumberDisplayHidden) : this(
        active = xsiNumberDisplayHidden.active ?: false,
    )
}