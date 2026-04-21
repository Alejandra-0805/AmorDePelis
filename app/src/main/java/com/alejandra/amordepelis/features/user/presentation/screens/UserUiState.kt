package com.alejandra.amordepelis.features.user.presentation.screens

data class UserProfileUiState(
    val id: String = "",
    val screenTitle: String = "Ajustes",
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val isDeleting: Boolean = false,
    val username: String = "",
    val email: String = "",
    val passwordMasked: String = "",
    val hasPartner: Boolean = false,
    val ownInviteCode: String = "",
    val roomName: String = "",
    val inviteCodeInput: String = "",
    val partnerUsername: String = "",
    val partnerEmail: String = "",
    val role: String = "",
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

data class UserAnnouncement(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String
)
