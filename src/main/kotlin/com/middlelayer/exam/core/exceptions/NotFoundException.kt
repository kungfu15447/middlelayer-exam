package com.middlelayer.exam.core.exceptions

class NotFoundException: Exception {
    constructor() : super()
    constructor(message: String) : super(message)
}