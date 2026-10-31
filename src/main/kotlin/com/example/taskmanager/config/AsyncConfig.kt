package com.example.taskmanager.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class AsyncConfig {

    @Bean(destroyMethod = "close")
    fun aggregateExecutor(): ExecutorService {
        return Executors.newFixedThreadPool(8)
    }
}
