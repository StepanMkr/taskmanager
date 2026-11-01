package com.example.taskmanager.exception

data class ApiError(
    val status: Int,
    val message: String
)