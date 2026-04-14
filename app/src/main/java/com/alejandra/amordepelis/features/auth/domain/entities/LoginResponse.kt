package com.alejandra.amordepelis.features.auth.domain.entities

data class LoginResponse(
    val role: String,
    val token: String
)
