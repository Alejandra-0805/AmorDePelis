package com.alejandra.amordepelis.features.auth.domain.entities

data class RegisterResponse(
    val id: Int,
    val email: String,
    val role: String,
    val username: String?,
    val profileImageUrl: String?
)
