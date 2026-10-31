package com.example.taskmanager.dto

data class PostDto(
    val userId: Long,
    val id: Long,
    val title: String,
    val body: String
)