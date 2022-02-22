package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.ExclusionNumber

data class DExclusionNumber(
    var number: String
) {
    constructor(xsiExclusionNumber: ExclusionNumber) : this(
        number = xsiExclusionNumber.number ?: ""
    )
}