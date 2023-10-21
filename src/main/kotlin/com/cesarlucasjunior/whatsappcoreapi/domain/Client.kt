package com.cesarlucasjunior.whatsappcoreapi.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Entity
data class Client(
    @Id
    @Column(length = 20)
    val id: String? = "",
    val lastMessageIn24hours: Boolean? = true,
    var selectedOptionId: String? = "",
    val createdAt: String = Clock.System.now().toLocalDateTime(TimeZone.of("America/Sao_Paulo")).toString(),
    )