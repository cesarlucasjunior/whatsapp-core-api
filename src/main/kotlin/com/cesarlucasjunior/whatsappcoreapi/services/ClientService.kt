package com.cesarlucasjunior.whatsappcoreapi.services

import com.cesarlucasjunior.whatsappcoreapi.ClientRepository
import com.cesarlucasjunior.whatsappcoreapi.domain.Client
import org.springframework.stereotype.Service

@Service
class ClientService(private val clientRepository: ClientRepository) {

    fun getClientInDataBase(id:String): Client? {
        val client = clientRepository.findById(id)
        return client.orElse(null)
    }

    fun save(client:Client): Client {
        return clientRepository.save(client)
    }

    fun saveServiceSelected(client: Client): Client {
        val clientDB = getClientInDataBase(client.id!!)
        clientDB?.phaseOfSelectedMenu = client.phaseOfSelectedMenu
        return save(clientDB!!)
    }

    fun saveSelectedMenuId(client: Client): Client {
        val clientDB = getClientInDataBase(client.id!!)
        clientDB?.selectedMenuId = client.selectedMenuId
        return save(clientDB!!)
    }
}