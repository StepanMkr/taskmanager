package com.example.taskmanager.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfig {
    fun cacheManager(): CacheManager {
        val manager = CaffeineCacheManager()

        manager.registerCustomCache(
            "users",
            Caffeine.newBuilder()
                .maximumSize(1)
                .expireAfterWrite(Duration.ofMinutes(5))
                .build()
        )

        manager.registerCustomCache(
            "posts",
            Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofSeconds(30))
                .build()
        )

        return manager
    }
}