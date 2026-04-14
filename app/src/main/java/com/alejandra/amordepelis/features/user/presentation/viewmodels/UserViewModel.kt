package com.alejandra.amordepelis.features.user.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.features.user.domain.usecases.UserUseCases
import com.alejandra.amordepelis.features.user.presentation.screens.PartnerSearchResultUiModel
import com.alejandra.amordepelis.features.user.presentation.screens.PartnerSearchUiState
import com.alejandra.amordepelis.features.user.presentation.screens.UserAnnouncement
import com.alejandra.amordepelis.features.user.presentation.screens.UserProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCases: UserUseCases
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(UserProfileUiState())
    val profileUiState: StateFlow<UserProfileUiState> = _profileUiState.asStateFlow()

    private val _partnerSearchUiState = MutableStateFlow(PartnerSearchUiState())
    val partnerSearchUiState: StateFlow<PartnerSearchUiState> = _partnerSearchUiState.asStateFlow()

    private val _announcements = MutableStateFlow<List<UserAnnouncement>>(emptyList())
    val announcements: StateFlow<List<UserAnnouncement>> = _announcements.asStateFlow()

    private val _currentAnnouncementIndex = MutableStateFlow(0)
    val currentAnnouncementIndex: StateFlow<Int> = _currentAnnouncementIndex.asStateFlow()

    init {
        loadAnnouncements()
    }

    private fun loadAnnouncements() {
        _announcements.value = listOf(
            UserAnnouncement(
                id = "1",
                title = "¡Conecta con tu pareja!",
                description = "Vincula tu cuenta para compartir tus películas favoritas",
                imageUrl = "https://images.unsplash.com/photo-1516589178581-6cd7833ae3b2?w=800"
            ),
            UserAnnouncement(
                id = "2",
                title = "Nuevas funciones",
                description = "Descubre las listas compartidas y comentarios en pareja",
                imageUrl = "https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?w=800"
            ),
            UserAnnouncement(
                id = "3",
                title = "Personaliza tu perfil",
                description = "Agrega tu foto y preferencias de películas",
                imageUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=800"
            )
        )
    }

    fun onAnnouncementIndexChange(index: Int) {
        _currentAnnouncementIndex.value = index
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _profileUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.getUserProfile() }
                .onSuccess { profile ->
                    _profileUiState.update {
                        it.copy(
                            isLoading = false,
                            id = profile.id,
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
        _profileUiState.update { it.copy(message = null, error = null) }
    }

    fun clearPartnerSearchMessage() {
        _partnerSearchUiState.update { it.copy(message = null) }
    }

    fun toggleEditMode() {
        _profileUiState.update { it.copy(isEditing = !it.isEditing) }
    }

    fun updateUsername(newUsername: String) {
        _profileUiState.update { it.copy(username = newUsername) }
    }

    fun saveProfile() {
        val currentState = _profileUiState.value
        if (currentState.username.isBlank()) {
            _profileUiState.update { it.copy(error = "El nombre de usuario no puede estar vacío") }
            return
        }
        
        viewModelScope.launch {
            _profileUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.updateUserProfile(currentState.id, currentState.username) }
                .onSuccess {
                    _profileUiState.update { it.copy(isEditing = false, message = "Perfil actualizado") }
                    loadUserProfile() // Reload context if necessary
                }
                .onFailure { throwable ->
                    _profileUiState.update { it.copy(isLoading = false, error = throwable.message ?: "Error al actualizar perfil") }
                }
        }
    }

    fun showDeleteDialog(show: Boolean) {
        _profileUiState.update { it.copy(showDeleteDialog = show) }
    }

    fun deleteProfile() {
        val currentState = _profileUiState.value
        viewModelScope.launch {
            _profileUiState.update { it.copy(isDeleting = true, error = null) }
            runCatching { useCases.deleteUser(currentState.id) }
                .onSuccess {
                    _profileUiState.update { it.copy(isDeleting = false, showDeleteDialog = false, message = "Perfil eliminado correctamente") }
                    // Here we ideally notify UI to log out
                }
                .onFailure { throwable ->
                    _profileUiState.update { it.copy(isDeleting = false, showDeleteDialog = false, error = throwable.message ?: "Error al eliminar perfil") }
                }
        }
    }
}
