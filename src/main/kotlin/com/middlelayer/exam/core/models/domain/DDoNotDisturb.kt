package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.DoNotDisturb

data class DDoNotDisturb(
    var active: Boolean
) {
    constructor(xsiDoNotDisturb: DoNotDisturb): this(
        active = xsiDoNotDisturb.active ?: false
    )
}