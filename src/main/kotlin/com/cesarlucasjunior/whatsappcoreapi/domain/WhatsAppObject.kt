package com.cesarlucasjunior.whatsappcoreapi.domain


data class WhatsAppObject(
    val `object`: String,
    val entry: List<Entry>
)

data class Entry(
    val id: String,
    val changes: List<Change>
)

data class Change(
    val value: Value
)

data class Value(
    val messaging_product: String,
    val metadata: Metadata? = null,
    val contacts: List<Contact>? = listOf(),
    val messages: List<Message>? = listOf(),
    val field: String? = "",
    val statuses: List<Status>?= listOf()
)

data class Status(
    val id: String,
    val status: String,
    val timestamp: String,
    val recipient_id: String,
    val conversation: Conversation,
    val pricing: Pricing
)

data class Conversation(
    val id: String,
    val expiration_timestamp: String?="",
    val origin: Origin
)

data class Origin(
    val type: String
)

data class Pricing(
    val billable: Boolean,
    val pricing_model: String,
    val category: String
)

data class Metadata(
    val display_phone_number: String,
    val phone_number_id: String
)
