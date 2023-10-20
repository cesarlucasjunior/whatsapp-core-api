package com.cesarlucasjunior.whatsappcoreapi.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Client(
    @Id
    @Column(length = 129)
    val id: String,
    val number: String,
    val lastMessageIn24hours: Boolean? =false
)