package com.cesarlucasjunior.whatsappcoreapi.controller

import com.cesarlucasjunior.whatsappcoreapi.client.SendingMessageFeign
import com.cesarlucasjunior.whatsappcoreapi.domain.DefaultMessage
import com.cesarlucasjunior.whatsappcoreapi.domain.ReadedWhatsAppMessage
import com.cesarlucasjunior.whatsappcoreapi.domain.WhatsAppDefaultText
import com.cesarlucasjunior.whatsappcoreapi.domain.WhatsAppObject
import com.google.gson.Gson
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @PostMapping
    fun recevingMessageFromClient(@RequestBody whatsAppObject: WhatsAppObject): ResponseEntity<String>  {
        if(whatsAppObject.entry[0].changes[0].value.statuses == null) {
            println("Message received! ${whatsAppObject.`object`}")
            val readedWhatsAppMessage = ReadedWhatsAppMessage(
                whatsAppObject.entry[0].changes[0].value.messaging_product, "read",
                whatsAppObject.entry[0].changes[0].value.messages!![0].id
            )
            val json = Gson().toJson(readedWhatsAppMessage)
            sendingMessageFeign.readingMessage(json)
            val resposta = DefaultMessage("whatsapp", "5561999125142", "text", WhatsAppDefaultText("Manda filhão!"))
            sendMessage(resposta)
        }
        return ResponseEntity.ok("")
    }

    @PostMapping("/send")
    fun sendMessage(@RequestBody defaultMessage: DefaultMessage): ResponseEntity<String> {

        val json = Gson().toJson(defaultMessage)
        println("message_product - $json")
        //println("token - $token")
        val response = sendingMessageFeign.sendMessage(json)

        return ResponseEntity.ok("Deu bom!")
    }

    //@PostMapping
    fun receiverClientMessage(@RequestBody whatsAppObject: WhatsAppObject): ResponseEntity<String> {
        //Verificar se o client existe e se tem comunicação nas últimas 24horas.
        return ResponseEntity.ok("Deu bom!")
    }
}