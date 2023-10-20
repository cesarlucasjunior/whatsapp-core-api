package com.cesarlucasjunior.whatsappcoreapi.client

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class WebConfig {

    fun webClient(authorization:String): WebClient {
        return WebClient.builder()
            .baseUrl("https://graph.facebook.com")
            .defaultHeader(HttpHeaders.AUTHORIZATION, authorization, HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}