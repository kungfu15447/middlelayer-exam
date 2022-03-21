package com.middlelayer.exam.infrastructure

import com.middlelayer.exam.core.exceptions.BadRequestException
import com.middlelayer.exam.core.exceptions.ISEException
import com.middlelayer.exam.core.exceptions.NotFoundException
import com.middlelayer.exam.core.exceptions.UnauthorizedException
import com.middlelayer.exam.core.models.xsi.ErrorInfo
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono

class WebClientConfiguration {
    private val objectParser = ObjectParser()

    fun handleApiErrorResponse(clientResponse: ClientResponse): Mono<ClientResponse> {
        var statusCode = clientResponse.statusCode()
        if (statusCode.isError) {
            return clientResponse.bodyToMono(String::class.java).flatMap {
                createExceptionForStatus(it, clientResponse.statusCode())
            }.switchIfEmpty(createExceptionForStatus(null, clientResponse.statusCode()))
        }
        return Mono.just(clientResponse)
    }

    private fun createExceptionForStatus(body: String?, statusCode: HttpStatus): Mono<ClientResponse> {
        var ex: Mono<ClientResponse>
        body.let {
            var errorMessage = it
            try {
                val errorInfo = objectParser.tryMapXml<ErrorInfo>(it)
                errorMessage = errorInfo.summaryEnglish ?: errorInfo.summary
            } catch (_: Exception) {

            }
            ex = when (statusCode) {
                HttpStatus.NOT_FOUND -> Mono.error(NotFoundException(errorMessage ?: "Tried to call non-existing endpoint"))
                HttpStatus.UNAUTHORIZED -> Mono.error(UnauthorizedException(errorMessage ?: "Tried to access unauthorized endpoint"))
                HttpStatus.INTERNAL_SERVER_ERROR -> Mono.error(ISEException(errorMessage ?: "Internal error at server endpoint"))
                else -> {
                    Mono.error(BadRequestException(errorMessage ?: "Not supported request body"))
                }
            }
        }
        return ex
    }
}