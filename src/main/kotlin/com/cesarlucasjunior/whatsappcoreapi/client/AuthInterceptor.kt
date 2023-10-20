package com.cesarlucasjunior.whatsappcoreapi.client

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.context.annotation.Configuration

@Configuration
class AuthInterceptor:RequestInterceptor {
    override fun apply(template: RequestTemplate?) {
        template?.header("Content-Type", "application/json")
        template?.header("Authorization",
            "Bearer EAAB63DZCfvMkBOz3fQs7f6Me9Uh7l1HiQsJvIBb7CknpgXfl22kZCer4IJGeG0ws4V1CGSZB7BZB3ZAiaj0QOQMgD4oswTzF4Kbe7MNcZByd7v4KtRbzWrFW3rGBIk0B6ca9ZA7tun49opCn8QDxJ0sZAOl86uZCUBJ3Mf6UGQmSV06pgdrIh6UTPT6dRVehh9T9d3JF0krvzmdWQZCL7QZB6ZA73i58RveMbiCBMF0ZD")
    }
}