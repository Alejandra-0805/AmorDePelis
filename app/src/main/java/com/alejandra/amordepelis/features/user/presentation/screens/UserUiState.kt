package com.alejandra.amordepelis.features.user.presentation.screens

data class UserProfileUiState(
    val screenTitle: String = "Ajustes",
    val isLoading: Boolean = false,
    val username: String = "",
    val email: String = "",
    val passwordMasked: String = "",
    val hasPartner: Boolean = false,
    val partnerUsername: String = "",
    val partnerEmail: String = "",
    val error: String? = null,
    val message: String? = null
)

data class PartnerSearchResultUiModel(
    val id: String,
    val username: String,
    val email: String,
    val isInviting: Boolean = false
)

data class PartnerSearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val results: List<PartnerSearchResultUiModel> = emptyList(),
    val error: String? = null,
    val message: String? = null
)
