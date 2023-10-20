package com.cesarlucasjunior.whatsappcoreapi.domain

import java.sql.Timestamp

data class Message(
    val from: String,
    val id: String,
    val timestamp: String,
    val text: WhatsAppDefaultText,
    val type: String
)
