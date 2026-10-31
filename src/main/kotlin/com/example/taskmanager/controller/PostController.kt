package com.example.taskmanager.controller

import com.example.taskmanager.client.PostClient
import com.example.taskmanager.client.UsersClient
import com.example.taskmanager.dto.EnrichedPostDto
import com.example.taskmanager.dto.PostDto
import com.example.taskmanager.dto.UserDto
import com.example.taskmanager.service.PostAggregationService
import com.example.taskmanager.service.PostSearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val aggregationService: PostAggregationService, private val searchService: PostSearchService
) {
    @GetMapping
    fun getPosts(
        @RequestParam(defaultValue = "1") page: Int, @RequestParam(defaultValue = "5") size: Int
    ): List<EnrichedPostDto> {
        require(page > 0) { "page must be positive" }
        require(size in 1..100) { "size must be between 1 and 100" }

        return aggregationService.getEnrichedPosts(page, size)
    }

    @GetMapping("/search")
    fun searchPosts(@RequestParam query: String): List<EnrichedPostDto> {
        return searchService.search(query)
    }
}