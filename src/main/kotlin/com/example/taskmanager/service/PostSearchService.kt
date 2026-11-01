package com.example.taskmanager.service

import com.example.taskmanager.client.PostClient
import com.example.taskmanager.client.UsersClient
import com.example.taskmanager.dto.EnrichedPostDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

@Service
class PostSearchService(
    private val postsClient: PostClient,
    private val usersClient: UsersClient,
    @param:Qualifier("aggregateExecutor")
    private val executor: Executor
) {

    companion object {
        // знаем это значение, на деле - API должен возвращать
        private const val TOTAL_POSTS = 100
        private const val PAGE_SIZE = 20
    }

    fun search(query: String): List<EnrichedPostDto> {

        val totalPages = (TOTAL_POSTS + PAGE_SIZE - 1) / PAGE_SIZE

        val pageFutures = (1..totalPages).map { page ->
            CompletableFuture.supplyAsync({
                postsClient.getPosts(page, PAGE_SIZE)
            }, executor)
        }

        val allPosts = CompletableFuture
            .allOf(*pageFutures.toTypedArray())
            .thenApply {
                pageFutures.flatMap { it.join() }
            }
            .join()

        val filteredPosts = allPosts.filter { post ->
            post.title.contains(query, ignoreCase = true) ||
                    post.body.contains(query, ignoreCase = true)
        }

        val usersById = usersClient.getUsers().associateBy { it.id }

        return filteredPosts.map { post ->
            val author = usersById[post.userId]

            EnrichedPostDto(
                id = post.id,
                userId = post.userId,
                title = post.title,
                body = post.body,
                authorName = author?.name,
                authorEmail = author?.email
            )
        }
    }
}