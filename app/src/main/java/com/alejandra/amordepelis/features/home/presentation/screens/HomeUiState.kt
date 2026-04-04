package com.alejandra.amordepelis.features.home.presentation.screens

data class HomeUiState(
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val secondName: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
