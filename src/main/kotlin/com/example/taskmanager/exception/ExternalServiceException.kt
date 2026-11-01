package com.example.taskmanager.exception

class ExternalServiceException(
    message: String,
    cause: Throwable
) : RuntimeException(message, cause)