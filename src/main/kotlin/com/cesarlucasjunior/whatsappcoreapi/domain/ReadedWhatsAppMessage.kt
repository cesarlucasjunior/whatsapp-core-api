package com.cesarlucasjunior.whatsappcoreapi.domain

data class ReadedWhatsAppMessage(
    val messaging_product: String,
    val status: String? = "read",
    val message_id: String
)
