package com.example.taskmanager.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig {

    @Bean
    fun jsonPlaceholderRestClient(
        @Value("\${external.jsonplaceholder.base-url}") baseUrl: String
    ): RestClient {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .build()
    }
}