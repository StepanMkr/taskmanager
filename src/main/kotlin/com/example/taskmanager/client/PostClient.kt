package com.example.taskmanager.client

import com.example.taskmanager.dto.PostDto
import com.example.taskmanager.exception.ExternalServiceException
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException

@Component
class PostClient(
    private val restClient: RestClient
) {

    @Cacheable(cacheNames = ["posts"], sync = true)
    fun getPosts(page: Int, size: Int): List<PostDto> {
        try {
            println("HTTP-запрос к Posts API")

            return restClient.get()
                .uri("/posts?_page={page}&_limit={size}", page, size)
                .retrieve()
                .body(Array<PostDto>::class.java)
                ?.toList()
                .orEmpty()
        } catch (ex: RestClientException) {
            throw ExternalServiceException(
                message = "Failed to fetch posts: page=$page, size=$size",
                cause = ex
            )
        }
    }
}