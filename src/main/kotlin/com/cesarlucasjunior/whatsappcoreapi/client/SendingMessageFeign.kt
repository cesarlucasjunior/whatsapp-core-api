package com.cesarlucasjunior.whatsappcoreapi.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader


@FeignClient(value = "sendingMessages", url = "https://graph.facebook.com/v18.0", configuration = [OpenFeignConfiguration::class])
interface SendingMessageFeign {

    @PostMapping("/142877365569867/messages")
    fun sendMessage(
        //@RequestHeader("Authorization") bearerToken: String,
        //@RequestHeader("Content-Type") contentType: String,
        @RequestBody defaultMessage: String
    )
}