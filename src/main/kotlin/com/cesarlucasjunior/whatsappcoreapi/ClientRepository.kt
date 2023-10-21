package com.cesarlucasjunior.whatsappcoreapi

import com.cesarlucasjunior.whatsappcoreapi.domain.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository: JpaRepository<Client, String> {

    fun findIsCreatedById(id: String): Boolean
}