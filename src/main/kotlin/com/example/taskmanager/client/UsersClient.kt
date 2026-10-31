package com.example.taskmanager.client

import com.example.taskmanager.dto.UserDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class UsersClient(
    private val restClient: RestClient
) {

    @Cacheable(cacheNames = ["users"], sync = true)
    fun getUsers(): List<UserDto> {
        println("HTTP-запрос к Users API")

        return restClient.get()
            .uri("/users")
            .retrieve()
            .body(Array<UserDto>::class.java)
            ?.toList()
            .orEmpty()
    }
}