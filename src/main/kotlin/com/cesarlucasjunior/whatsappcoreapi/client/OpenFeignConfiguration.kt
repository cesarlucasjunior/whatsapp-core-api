package com.cesarlucasjunior.whatsappcoreapi.client

import feign.Logger
import org.springframework.context.annotation.Bean

class OpenFeignConfiguration {
    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }
}