package com.cesarlucasjunior.whatsappcoreapi.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
data class Client(
    @Id
    @Column(length = 20)
    val id: String? = "",
    val lastMessageIn24hours: Boolean? = true,
    var selectedMenuId: String? = "",
    var phaseOfSelectedMenu: Int? = 0,
    val createdAt: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm"))
)