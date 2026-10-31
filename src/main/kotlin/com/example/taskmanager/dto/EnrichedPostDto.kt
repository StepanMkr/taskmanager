package com.example.taskmanager.dto

data class EnrichedPostDto(
    val id: Long,
    val userId: Long,
    val title: String,
    val body: String,
    val authorName: String?,
    val authorEmail: String?
)