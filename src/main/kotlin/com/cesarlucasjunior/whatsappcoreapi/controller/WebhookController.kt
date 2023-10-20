package com.cesarlucasjunior.whatsappcoreapi.controller

import com.cesarlucasjunior.whatsappcoreapi.client.SendingMessageFeign
import com.cesarlucasjunior.whatsappcoreapi.client.WebConfig
import com.cesarlucasjunior.whatsappcoreapi.domain.DefaultMessage
import com.google.gson.Gson
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
@RequestMapping("/webhooks")
class WebhookController(private val sendingMessageFeign: SendingMessageFeign) {

    @GetMapping
    fun healthStatus(@RequestParam("hub.challenge") testChallenge:String): ResponseEntity<String> {
        println("Acionou")
        print(testChallenge)
        return ResponseEntity.ok(testChallenge)
    }

    @PostMapping("/send")
    fun sendMessage(@RequestBody defaultMessage: DefaultMessage,
                    //@RequestHeader("Authorization") token: String,
                    @RequestHeader("Content-Type") contentType: String): ResponseEntity<String> {

        val json = Gson().toJson(defaultMessage)
        println("message_product - $json")
        //println("token - $token")
        val response = sendingMessageFeign.sendMessage(json)


        return ResponseEntity.ok("Deu bom!")
    }
}