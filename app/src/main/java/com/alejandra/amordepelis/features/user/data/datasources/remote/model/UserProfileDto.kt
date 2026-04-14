package com.alejandra.amordepelis.features.user.data.datasources.remote.model

data class UserProfileDto(
    val id: String,
    val username: String,
    val email: String,
    val passwordMasked: String,
    val partner: PartnerProfileDto? = null
)
