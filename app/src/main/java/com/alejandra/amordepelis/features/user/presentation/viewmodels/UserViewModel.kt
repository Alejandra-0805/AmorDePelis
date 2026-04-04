package com.alejandra.amordepelis.features.user.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.features.user.di.UserUseCaseModule
import com.alejandra.amordepelis.features.user.domain.usecases.UserUseCases
import com.alejandra.amordepelis.features.user.presentation.screens.PartnerSearchResultUiModel
import com.alejandra.amordepelis.features.user.presentation.screens.PartnerSearchUiState
import com.alejandra.amordepelis.features.user.presentation.screens.UserProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val useCases: UserUseCases = UserUseCaseModule.provideUserUseCases()
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(UserProfileUiState())
    val profileUiState = _profileUiState.asStateFlow()

    private val _partnerSearchUiState = MutableStateFlow(PartnerSearchUiState())
    val partnerSearchUiState = _partnerSearchUiState.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            _profileUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.getUserProfile() }
                .onSuccess { profile ->
                    _profileUiState.update {
                        it.copy(
                            isLoading = false,
                            username = profile.username,
                            email = profile.email,
                            passwordMasked = profile.passwordMasked,
                            hasPartner = profile.partner != null,
                            partnerUsername = profile.partner?.username.orEmpty(),
                            partnerEmail = profile.partner?.email.orEmpty()
                        )
                    }
                }
                .onFailure { throwable ->
                    _profileUiState.update {
                        it.copy(isLoading = false, error = throwable.message ?: "Error loading profile")
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _partnerSearchUiState.update { it.copy(searchQuery = query, error = null) }
    }

    fun searchUsersByUsername() {
        val query = _partnerSearchUiState.value.searchQuery.trim()
        if (query.isBlank()) {
            _partnerSearchUiState.update { it.copy(error = "Ingresa un username para buscar") }
            return
        }

        viewModelScope.launch {
            _partnerSearchUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.searchUsersByUsername(query) }
                .onSuccess { users ->
                    _partnerSearchUiState.update {
                        it.copy(
                            isLoading = false,
                            results = users.map { user ->
                                PartnerSearchResultUiModel(
                                    id = user.id,
                                    username = user.username,
                                    email = user.email
                                )
                            }
                        )
                    }
                }
                .onFailure { throwable ->
                    _partnerSearchUiState.update {
                        it.copy(isLoading = false, error = throwable.message ?: "Error searching users")
                    }
                }
        }
    }

    fun sendPartnerInvitation(userId: String) {
        viewModelScope.launch {
            _partnerSearchUiState.update { state ->
                state.copy(
                    results = state.results.map {
                        if (it.id == userId) it.copy(isInviting = true) else it
                    }
                )
            }

            runCatching { useCases.sendPartnerInvitation(userId) }
                .onSuccess {
                    _partnerSearchUiState.update { state ->
                        state.copy(
                            message = "Invitacion enviada",
                            results = state.results.map {
                                if (it.id == userId) it.copy(isInviting = false) else it
                            }
                        )
                    }
                }
                .onFailure { throwable ->
                    _partnerSearchUiState.update { state ->
                        state.copy(
                            error = throwable.message ?: "Error sending invitation",
                            results = state.results.map {
                                if (it.id == userId) it.copy(isInviting = false) else it
                            }
                        )
                    }
                }
        }
    }

    fun clearProfileMessage() {
        _profileUiState.update { it.copy(message = null) }
    }

    fun clearPartnerSearchMessage() {
        _partnerSearchUiState.update { it.copy(message = null) }
    }
}
