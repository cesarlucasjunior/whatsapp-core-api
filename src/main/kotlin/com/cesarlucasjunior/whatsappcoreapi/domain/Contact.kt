package com.cesarlucasjunior.whatsappcoreapi.domain

data class Contact(
    val profile: Profile,
    val wa_id: String
)

data class Profile(
    val name: String
)
