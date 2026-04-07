package com.alejandra.amordepelis.features.auth.data.datasources.remote.model

data class RegisterResponseDto(
    val id: Int,
    val email: String,
    val role: String,
    val username: String?,
    val profileImageUrl: String?
)
