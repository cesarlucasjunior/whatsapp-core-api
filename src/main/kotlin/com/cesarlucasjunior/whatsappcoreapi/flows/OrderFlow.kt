package com.cesarlucasjunior.whatsappcoreapi.flows

import com.cesarlucasjunior.whatsappcoreapi.client.SendingMessageFeign
import com.cesarlucasjunior.whatsappcoreapi.domain.Client
import com.cesarlucasjunior.whatsappcoreapi.domain.DefaultMessage
import com.cesarlucasjunior.whatsappcoreapi.domain.WhatsAppDefaultText
import com.cesarlucasjunior.whatsappcoreapi.domain.WhatsAppObject
import com.cesarlucasjunior.whatsappcoreapi.services.ClientService
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class OrderFlow {

    fun startFlow(whatsAppObject: WhatsAppObject, client: Client,clientService: ClientService, sendingMessageFeign: SendingMessageFeign) {
        //Verificar em qual ponto do fluxo de compra ele está
        val client = clientService.getClientInDataBase(client.id!!)
        when(client?.phaseOfSelectedMenu) {
            0 -> startConversation(whatsAppObject, client, clientService, sendingMessageFeign)
        }

    }

    private fun startConversation(whatsAppObject: WhatsAppObject, client: Client,clientService: ClientService, sendingMessageFeign: SendingMessageFeign) {
        println("Starting conversation...")
        println("$whatsAppObject")
        //readedMessage(whatsAppObject)
        val resposta = DefaultMessage("whatsapp", "5561999125142", "text",
            WhatsAppDefaultText("Fico feliz de escolher a ClickBus! Poderia me informar de" +
                    "qual cidade sairá?"))
        val json = Gson().toJson(resposta)
        val response = sendingMessageFeign.sendMessage(json)
        clientService.saveSelectedMenuId(client)
    }


}