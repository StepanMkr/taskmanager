package com.example.taskmanager.service

import com.example.taskmanager.client.PostClient
import com.example.taskmanager.client.UsersClient
import com.example.taskmanager.dto.EnrichedPostDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

@Service
class PostAggregationService(
    val postClient: PostClient,
    val usersClient: UsersClient,
    @param:Qualifier("aggregateExecutor")
    val aggregationExecutor: Executor
) {
    fun getEnrichedPosts(page: Int, size: Int): List<EnrichedPostDto> {
        val postsFuture = CompletableFuture.supplyAsync({
            postClient.getPosts(page, size)
        }, aggregationExecutor)

        val usersFuture = CompletableFuture.supplyAsync({
            usersClient.getUsers()
        }, aggregationExecutor)

        return postsFuture.thenCombine(usersFuture) { posts, users ->
            val usersById = users.associateBy { it.id }

            posts.map { post ->
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
        }.join()
    }
}
