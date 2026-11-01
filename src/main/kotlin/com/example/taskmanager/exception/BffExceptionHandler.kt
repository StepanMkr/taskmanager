package com.example.taskmanager.exception

import com.example.taskmanager.controller.PostController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.concurrent.CompletionException

@RestControllerAdvice(
    assignableTypes = [PostController::class]
)
class BffExceptionHandler {

    @ExceptionHandler(
        ExternalServiceException::class,
        CompletionException::class
    )
    fun handleError(ex: Exception): ResponseEntity<ApiError> {
        val originalException = if (ex is CompletionException) {
            ex.cause
        } else {
            ex
        }

        return if (originalException is ExternalServiceException) {
            ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(
                    ApiError(
                        status = 502,
                        message = "External service unavailable"
                    )
                )
        } else {
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    ApiError(
                        status = 500,
                        message = "Internal BFF error"
                    )
                )
        }
    }
}