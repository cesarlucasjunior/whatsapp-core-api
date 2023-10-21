package com.cesarlucasjunior.whatsappcoreapi.services

import com.cesarlucasjunior.whatsappcoreapi.ClientRepository
import org.springframework.stereotype.Service

@Service
class LastComunicationService(private val clientRepository: ClientRepository) {

    fun lastComunicationIn24Hours(id:String):Boolean {
        return clientRepository.findIsCreatedById(id)
    }
}