package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.ims.NumberDisplay

data class DNumberDisplay(
    var currentNumber: String,
    var displayType: String,
) {
    constructor(
        xsiNumberDisplay: NumberDisplay
    ) : this(
        currentNumber = xsiNumberDisplay.presentationNumber ?: "",
        displayType = xsiNumberDisplay.presentationStatus ?: ""
    )
}

