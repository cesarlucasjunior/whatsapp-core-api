package com.cesarlucasjunior.whatsappcoreapi.controller

import com.cesarlucasjunior.whatsappcoreapi.client.SendingMessageFeign
import com.cesarlucasjunior.whatsappcoreapi.domain.*
import com.cesarlucasjunior.whatsappcoreapi.flows.*
import com.cesarlucasjunior.whatsappcoreapi.services.ClientService
import com.google.gson.Gson
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
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
//    fun recevingMessageFromClient(@RequestBody whatsAppObject: WhatsAppObject): ResponseEntity<String>  {
//        if(whatsAppObject.entry[0].changes[0].value.statuses == null) {
//            println("Message received! ${whatsAppObject.`object`}")
//            val readedWhatsAppMessage = ReadedWhatsAppMessage(
//                whatsAppObject.entry[0].changes[0].value.messaging_product, "read",
//                whatsAppObject.entry[0].changes[0].value.messages!![0].id
//            )
//            val json = Gson().toJson(readedWhatsAppMessage)
//            sendingMessageFeign.readingMessage(json)
//            val resposta = DefaultMessage("whatsapp", "5561999125142", "text", WhatsAppDefaultText("Manda filhão!"))
//            sendMessage(resposta)
//        }
//        return ResponseEntity.ok("")
//    }

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
            println("Is not a status webhook message! It's a webhook event!")
            val clientContact = whatsAppObject.entry[0].changes[0].value.contacts?.get(0)
            val client = Client(id = clientContact?.wa_id)
            //Verificar se o client existe e se tem comunicação nas últimas 24horas.
            val clientDB = clientService.getClientInDataBase(clientContact!!.wa_id)
            if (clientDB !== null) {
                println("Cliente já existe na nossa base...")
                if (clientDB.lastMessageIn24hours == true) {
                    //Qual ponto do papo ele está?
                    when(clientDB.selectedMenuId) {
                        "" -> {
                            println("Sem menu selecionado!")
                            MenuFlow(whatsAppObject, client, clientService, sendingMessageFeign).startFlow()
                        }
                        "row_1_comprar_passagem" -> {
                            println("Fluxo de compra de passagem!")
                            OrderFlow().startFlow(whatsAppObject, client, clientService, sendingMessageFeign)
                        }
                        "row_3_cancelamento" -> {
                            println("Fluxo de cancelamento de compra!")
                            CancellationFlow(whatsAppObject, client)
                        }
                        "row_4_perguntas_frequentes" -> {
                            println("Fluxo de perguntas frequentes")
                            CommonQuestionsFlow(whatsAppObject, client)
                        }
                    }
                }
            } else {
                println("Cliente nunca conversou via whatsApp")
                val clientSaved = clientService.save(client)
                println("Client ${clientSaved.id} saved!")
                greeting(whatsAppObject, clientContact)
            }
        } // É uma mensagem de status
        return ResponseEntity.ok("Deu bom!")
    }

    private fun selectedOptionsManager(selectedResponse: String?, whatsAppObject: WhatsAppObject, client: Client) {
        when(selectedResponse) {
            "row_1_comprar_passagem" -> OrderFlow().startFlow(whatsAppObject, client, clientService, sendingMessageFeign)
            "row_2_atendimento" -> ServiceFlow(whatsAppObject, client)
            "row_3_cancelamento" -> CancellationFlow(whatsAppObject, client)
            "row_4_perguntas_frequentes" -> CommonQuestionsFlow(whatsAppObject, client)
        }
    }

    private fun isStatusMessage(whatsAppObject: WhatsAppObject): Boolean {
        return !whatsAppObject.entry[0].changes[0].value.statuses.isNullOrEmpty()
    }

    fun greeting(whatsAppObject: WhatsAppObject, contact: Contact?) {
        println("Sending greetings...")
        readedMessage(whatsAppObject)
        val resposta = DefaultMessage("whatsapp", "5561999125142", "text", WhatsAppDefaultText("Olá ${contact?.getName()}! Bem-vindo à Clickbus!"))
        sendMessage(resposta)
        listingOptionsOfService(whatsAppObject, contact)
    }

    private fun listingOptionsOfService(whatsAppObject: WhatsAppObject, contact: Contact?) {
        println("Sending list of services")
        val row1 = WhatsAppSectionRow("row_1_comprar_passagem", title = "Comprar passagem", "")
        val row2 = WhatsAppSectionRow("row_2_atendimento", title = "Atendimento", "")
        val row3 = WhatsAppSectionRow("row_3_cancelamento", title = "Cancelamento", "")
        val row4 = WhatsAppSectionRow("row_4_perguntas_frequentes", title = "Perguntas Frequentes", "")

        val section = Section("Lista de opções", listOf(row1, row2, row3, row4) )
        val interactive = ListIteractiveWhatsAppMessage(
            body = BodyWhatsAppMessage("Como podemos te ajudar?"),
            action = ActionWhatsAppMessage("Lista de opções", listOf(section))
        )
        val iteractiveWhatsAppMessage = IteractiveWhatsAppMessage(
            interactive = interactive
        )
        val json = Gson().toJson(iteractiveWhatsAppMessage)
        sendingMessageFeign.sendMessage(json)
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