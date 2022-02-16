package com.middlelayer.exam.core.exceptions

class UnauthorizedException: Exception {
    constructor() : super()
    constructor(message: String) : super(message)
}