package com.cesarlucasjunior.whatsappcoreapi.domain

data class Contact(
    val profile: Profile,
    val wa_id: String
) {
    fun getName():String {
        return profile.name
    }
}

data class Profile(
    val name: String
)
