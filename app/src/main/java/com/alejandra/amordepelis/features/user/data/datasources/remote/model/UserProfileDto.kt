package com.alejandra.amordepelis.features.user.data.datasources.remote.model

data class UserProfileDto(
    val id: Int,
    val email: String,
    val role: String? = null,
    val username: String? = null,
    val profileImageUrl: String? = null,
    val passwordMasked: String? = null,
    val partner: PartnerProfileDto? = null
)
