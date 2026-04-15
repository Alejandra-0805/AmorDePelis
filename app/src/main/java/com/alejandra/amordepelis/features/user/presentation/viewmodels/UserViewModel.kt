package com.alejandra.amordepelis.features.user.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.features.user.domain.usecases.UserUseCases
import com.alejandra.amordepelis.core.storage.SessionManager
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
    private val useCases: UserUseCases,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _profileUiState = MutableStateFlow(UserProfileUiState())
    val profileUiState: StateFlow<UserProfileUiState> = _profileUiState.asStateFlow()

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
                            partnerEmail = profile.partner?.email.orEmpty(),
                            ownInviteCode = profile.ownInviteCode.orEmpty(),
                            roomName = profile.roomName ?: "Sin sala asignada"
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

    fun clearProfileMessage() {
        _profileUiState.update { it.copy(message = null, error = null) }
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
                    loadUserProfile()
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
                }
                .onFailure { throwable ->
                    _profileUiState.update { it.copy(isDeleting = false, showDeleteDialog = false, error = throwable.message ?: "Error al eliminar perfil") }
                }
        }
    }

    fun onInviteCodeChange(newCode: String) {
        _profileUiState.update { it.copy(inviteCodeInput = newCode) }
    }

    fun joinRoom() {
        val code = _profileUiState.value.inviteCodeInput.trim()
        
        if (code.isBlank() || code == _profileUiState.value.ownInviteCode) {
            _profileUiState.update { it.copy(error = "El código ingresado es inválido o es tu propio código.") }
            return
        }

        viewModelScope.launch {
            _profileUiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                useCases.joinVirtualRoom(code) 
            }.onSuccess { 
                _profileUiState.update { 
                    it.copy(
                        isLoading = false,
                        hasPartner = true, 
                        message = "¡Pareja vinculada exitosamente!",
                        inviteCodeInput = ""
                    ) 
                }
                loadUserProfile()
            }.onFailure { throwable ->
                _profileUiState.update { 
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "Parece que este código no existe o la sala ya está llena."
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.logout()
        }
    }
}
