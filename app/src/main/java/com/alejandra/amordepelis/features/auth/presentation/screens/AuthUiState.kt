package com.alejandra.amordepelis.features.auth.presentation.screens

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val secondName: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
