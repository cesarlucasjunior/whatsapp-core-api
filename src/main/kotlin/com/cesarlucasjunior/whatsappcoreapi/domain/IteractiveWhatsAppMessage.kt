package com.cesarlucasjunior.whatsappcoreapi.domain

data class IteractiveWhatsAppMessage(
    val messaging_product: String = "whatsapp",
    val recipient_type: String = "individual",
    val to: String = "5561999125142",
    val type: String = "interactive",
    val interactive: ListIteractiveWhatsAppMessage
)

data class ListIteractiveWhatsAppMessage(
    val type:String = "list",
    val header: HeaderWhatsAppMessage? = null,
    val body: BodyWhatsAppMessage,
    val footer: FooterWhatsAppMessage? = null,
    val action: ActionWhatsAppMessage
)

data class HeaderWhatsAppMessage(
    val type: String = "text",
    val text: String
)

data class BodyWhatsAppMessage(
    val text: String
)

data class FooterWhatsAppMessage(
    val text: String
)

data class ActionWhatsAppMessage(
    val button: String,
    val sections: List<Section>
)

data class Section(
    val title: String,
    val rows: List<WhatsAppSectionRow>
)

data class WhatsAppSectionRow(
    val id: String,
    val title: String,
    val description: String
)