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
}