package com.cesarlucasjunior.whatsappcoreapi.flows

import com.cesarlucasjunior.whatsappcoreapi.domain.Client
import com.cesarlucasjunior.whatsappcoreapi.domain.WhatsAppObject

class CancellationFlow(
    val whatsAppObject: WhatsAppObject,
    val client: Client
) {
}