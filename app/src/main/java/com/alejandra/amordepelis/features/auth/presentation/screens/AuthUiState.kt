package com.alejandra.amordepelis.features.auth.presentation.screens

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
