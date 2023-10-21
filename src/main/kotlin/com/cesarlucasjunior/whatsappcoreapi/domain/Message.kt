package com.cesarlucasjunior.whatsappcoreapi.domain

import java.sql.Timestamp

data class Message(
    val from: String,
    val id: String,
    val timestamp: String,
    val text: WhatsAppDefaultText? = null,
    val type: String,
    val context: Context? = null,
    val interactive: InteractiveResponseWhatsApp? = null
)

data class InteractiveResponseWhatsApp(
    val type: String,
    val list_reply: Reply
)

data class Reply(
    val id: String,
    val title: String
)

data class Context(
    val from: String,
    val id: String
)
