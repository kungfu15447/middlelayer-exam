package com.middlelayer.exam.web

import com.middlelayer.exam.core.exceptions.BadRequestException
import com.middlelayer.exam.core.exceptions.ISEException
import com.middlelayer.exam.core.exceptions.NotFoundException
import com.middlelayer.exam.core.exceptions.UnauthorizedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorControllerAdvice {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(e: NotFoundException): ResponseEntity<String> {
        return if (e.message != null) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<String> {
        return if (e.message != null) {
            ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)
        } else {
            ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException): ResponseEntity<String> {
        return if (e.message != null) {
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        } else {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @ExceptionHandler(ISEException::class)
    fun handleInternalServerErrorException(e: ISEException): ResponseEntity<String> {
        return if (e.message != null) {
            ResponseEntity(e.message, HttpStatus.INTERNAL_SERVER_ERROR)
        } else {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}