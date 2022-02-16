package com.middlelayer.exam.core.exceptions

class BadRequestException: Exception {
    constructor() : super()
    constructor(message: String) : super(message)
}