package com.middlelayer.exam.core.models.domain

import com.middlelayer.exam.core.models.xsi.Service

data class DService (val name: String?) {
    constructor(xsiService: Service): this(
        name = xsiService.name
    )
}