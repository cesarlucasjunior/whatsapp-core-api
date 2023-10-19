package com.cesarlucasjunior.whatsappcoreapi.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
@RequestMapping("/webhooks")
class WebhookController {

    @GetMapping
    fun healthStatus(@RequestParam("hub.challenge") testChallenge:String): ResponseEntity<String> {
        println("Acionou")
        print(testChallenge)
        return ResponseEntity.ok(testChallenge)
    }
}