package com.cesarlucasjunior.whatsappcoreapi.domain


data class DefaultMessage(
    val messaging_product: String,
    val to: String,
    val type: String? = "text",
    val text: WhatsAppDefaultText
)

data class WhatsAppDefaultText(
    val body: String
)
