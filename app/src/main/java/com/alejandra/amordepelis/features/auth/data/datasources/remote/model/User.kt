package com.alejandra.amordepelis.features.auth.data.datasources.remote.model

data class User (
    val username: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)