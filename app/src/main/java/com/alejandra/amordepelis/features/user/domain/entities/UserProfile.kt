package com.alejandra.amordepelis.features.user.domain.entities

data class UserProfile(
    val id: String,
    val username: String,
    val email: String,
    val passwordMasked: String,
    val partner: PartnerProfile? = null
)
