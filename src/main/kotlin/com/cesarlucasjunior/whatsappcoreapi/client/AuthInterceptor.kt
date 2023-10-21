package com.cesarlucasjunior.whatsappcoreapi.client

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AuthInterceptor:RequestInterceptor {

    @Value("\${whatsapp_api_token}")
    private lateinit var authorization: String
    override fun apply(template: RequestTemplate?) {
        template?.header("Content-Type", "application/json")
        template?.header("Authorization",authorization)
    }
}