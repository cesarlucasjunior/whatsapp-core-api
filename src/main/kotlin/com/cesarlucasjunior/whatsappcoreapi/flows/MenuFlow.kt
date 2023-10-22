package com.cesarlucasjunior.whatsappcoreapi.flows

import com.cesarlucasjunior.whatsappcoreapi.client.SendingMessageFeign
import com.cesarlucasjunior.whatsappcoreapi.domain.Client
import com.cesarlucasjunior.whatsappcoreapi.domain.WhatsAppObject
import com.cesarlucasjunior.whatsappcoreapi.services.ClientService

class MenuFlow(
    val whatsAppObject: WhatsAppObject,
    val client: Client,
    val clientService: ClientService,
    val sendingMessageFeign: SendingMessageFeign
) {

    fun startFlow(){
        getResponseOfOptionsList(whatsAppObject, client, sendingMessageFeign)
    }

    private fun getResponseOfOptionsList(whatsAppObject: WhatsAppObject, client: Client, sendingMessageFeign:SendingMessageFeign) {
        // Pegar a resposta em relação a opção do menu selecionada:
        println("Resposta do usuário!")
        val selectedResponse = whatsAppObject.entry[0].changes[0].value.messages?.get(0)?.interactive?.list_reply?.id
        client.selectedMenuId = selectedResponse
        clientService.saveServiceSelected(client)
        //selectedOptionsManager(selectedResponse, whatsAppObject, client)
        when(selectedResponse) {
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
}