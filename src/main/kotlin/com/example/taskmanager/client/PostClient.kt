package com.example.taskmanager.client

import com.example.taskmanager.dto.PostDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class PostClient(
    private val restClient: RestClient
) {

    @Cacheable(cacheNames = ["posts"], sync = true)
    fun getPosts(page: Int, size: Int): List<PostDto> {
        return restClient.get()
            .uri("/posts?_page={page}&_limit={size}", page, size)
            .retrieve()
            .body(Array<PostDto>::class.java)
            ?.toList()
            .orEmpty()
    }
}