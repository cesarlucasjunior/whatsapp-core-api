package com.cesarlucasjunior.whatsappcoreapi.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient(value = "sendingMessages", url = "https://graph.facebook.com/v18.0", configuration = [OpenFeignConfiguration::class])
interface SendingMessageFeign {

    @PostMapping("/142877365569867/messages")
    fun sendMessage(
        @RequestBody defaultMessage: String
    )

    @PostMapping("/142877365569867/messages")
    fun readingMessage(@RequestBody readedMessage: String)
}