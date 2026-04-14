package com.alejandra.amordepelis.features.auth.data.datasources.remote.model

data class RegisterRequest(
    val email: String,
    val passwordRaw: String,
    val role: String,
    val username: String
)
