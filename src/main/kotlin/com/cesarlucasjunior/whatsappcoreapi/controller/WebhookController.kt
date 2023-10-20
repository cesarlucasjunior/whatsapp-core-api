package com.cesarlucasjunior.whatsappcoreapi.controller

import com.cesarlucasjunior.whatsappcoreapi.client.SendingMessageFeign
import com.cesarlucasjunior.whatsappcoreapi.domain.*
import com.cesarlucasjunior.whatsappcoreapi.services.ClientService
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
class WebhookController(private val sendingMessageFeign: SendingMessageFeign,
                        private val clientService: ClientService) {

    @GetMapping
    fun healthStatus(@RequestParam("hub.challenge") testChallenge:String): ResponseEntity<String> {
        println("Acionou")
        print(testChallenge)
        return ResponseEntity.ok(testChallenge)
    }

    //@PostMapping
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

    @PostMapping
    fun receiverClientMessage(@RequestBody whatsAppObject: WhatsAppObject): ResponseEntity<String> {
        if (!isStatusMessage(whatsAppObject)) {
            println("Is not a status webhook message!")
            val clientContact = whatsAppObject.entry[0].changes[0].value.contacts?.get(0)
            val client = Client(id = clientContact?.wa_id)
            //Verificar se o client existe e se tem comunicação nas últimas 24horas.
            val clientDB = clientService.getClientInDataBase(clientContact!!.wa_id)
            if (clientDB !== null) {
                println("Cliente já existe na nossa base...")
                if (clientDB.lastMessageIn24hours == true) {
                    //Qual ponto do papo ele está?
                    println("Mensagem aberta!")
                }
            } else {
                println("Cliente nunca conversou via whatsApp")
                val clientSaved = clientService.save(client)
                println("Client ${clientSaved.id} saved!")
                greeting(whatsAppObject, clientContact)
            }
        }
        return ResponseEntity.ok("Deu bom!")
    }

    private fun isStatusMessage(whatsAppObject: WhatsAppObject): Boolean {
        return !whatsAppObject.entry[0].changes[0].value.statuses.isNullOrEmpty()
    }

    fun greeting(whatsAppObject: WhatsAppObject, contact: Contact?) {
        println("Sending greetings...")
        readedMessage(whatsAppObject)
        val resposta = DefaultMessage("whatsapp", "5561999125142", "text", WhatsAppDefaultText("Olá ${contact?.getName()}! No que posso ajudar?"))
        sendMessage(resposta)
    }

    fun readedMessage(whatsAppObject: WhatsAppObject) {
        val readedWhatsAppMessage = ReadedWhatsAppMessage(
            whatsAppObject.entry[0].changes[0].value.messaging_product, "read",
            whatsAppObject.entry[0].changes[0].value.messages!![0].id
        )
        val json = Gson().toJson(readedWhatsAppMessage)
        sendingMessageFeign.readingMessage(json)
    }
}