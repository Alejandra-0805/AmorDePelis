package com.alejandra.amordepelis.features.lists.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.core.storage.SessionManager
import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.entities.UpdateListParams
import com.alejandra.amordepelis.features.lists.domain.usecases.ListsUseCases
import com.alejandra.amordepelis.features.lists.presentation.screens.AddListUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.AnnouncementUiModel
import com.alejandra.amordepelis.features.lists.presentation.screens.DeleteListModalUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.EditListModalUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.ListDetailsUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.ListsScreenUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.SharedListItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(
    private val useCases: ListsUseCases,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _listsUiState = MutableStateFlow(ListsScreenUiState())
    val listsUiState: StateFlow<ListsScreenUiState> = _listsUiState.asStateFlow()

    private val _addListUiState = MutableStateFlow(AddListUiState())
    val addListUiState: StateFlow<AddListUiState> = _addListUiState.asStateFlow()

    private val _detailsUiState = MutableStateFlow(ListDetailsUiState())
    val detailsUiState: StateFlow<ListDetailsUiState> = _detailsUiState.asStateFlow()

    private val _editModalUiState = MutableStateFlow(EditListModalUiState())
    val editModalUiState: StateFlow<EditListModalUiState> = _editModalUiState.asStateFlow()

    private val _deleteModalUiState = MutableStateFlow(DeleteListModalUiState())
    val deleteModalUiState: StateFlow<DeleteListModalUiState> = _deleteModalUiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _announcements = MutableStateFlow<List<AnnouncementUiModel>>(emptyList())
    val announcements: StateFlow<List<AnnouncementUiModel>> = _announcements.asStateFlow()

    private val _currentAnnouncementIndex = MutableStateFlow(0)
    val currentAnnouncementIndex: StateFlow<Int> = _currentAnnouncementIndex.asStateFlow()

    init {
        updatePermissions()
    }

    private fun updatePermissions() {
        viewModelScope.launch {
            sessionManager.currentRole.collect { role ->
                _listsUiState.update { 
                    it.copy(
                        canCreateLists = role.canCreateLists(),
                        canEditLists = role.canCreateLists(),
                        canDeleteLists = role.canCreateLists()
                    )
                }
            }
        }
    }

    // Funciones de permisos para la UI
    fun canCreateLists(): Boolean = sessionManager.canCreateLists()

    fun loadSharedLists() {
        val roomId = sessionManager.currentRoomId.value
        if (roomId == null) {
            _error.value = "No estás en una sala. Por favor, crea o únete a una sala."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _listsUiState.update { it.copy(isLoading = true, error = null) }
            
            runCatching { useCases.getSharedLists(roomId) }
                .onSuccess { lists ->
                    _listsUiState.update {
                        it.copy(
                            isLoading = false,
                            lists = lists.map { list ->
                                SharedListItemUiModel(
                                    id = list.id,
                                    name = list.name,
                                    description = list.description,
                                    movieCount = list.movieCount,
                                    colorHex = list.colorHex
                                )
                            }
                        )
                    }
                    _isLoading.value = false
                }
                .onFailure { throwable ->
                    _listsUiState.update {
                        it.copy(isLoading = false, error = throwable.message ?: "Error loading lists")
                    }
                    _isLoading.value = false
                    _error.value = throwable.message ?: "Error loading lists"
                }
        }
    }

    fun loadAnnouncements() {
        viewModelScope.launch {
            runCatching { useCases.getAnnouncements() }
                .onSuccess { announcements ->
                    _announcements.value = announcements.map { announcement ->
                        AnnouncementUiModel(
                            id = announcement.id,
                            title = announcement.title,
                            description = announcement.description,
                            imageUrl = announcement.imageUrl
                        )
                    }
                }
                .onFailure { /* Ignore announcement loading errors */ }
        }
    }

    fun loadListDetails(listId: String) {
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.getSharedListDetails(listId) }
                .onSuccess { details ->
                    _detailsUiState.update {
                        it.copy(
                            isLoading = false,
                            listId = details.id,
                            listName = details.name,
                            description = details.description,
                            colorHex = details.colorHex,
                            movies = details.movies
                        )
                    }
                }
                .onFailure { throwable ->
                    _detailsUiState.update {
                        it.copy(isLoading = false, error = throwable.message ?: "Error loading list details")
                    }
                }
        }
    }

    fun onNewListNameChange(value: String) {
        _addListUiState.update { it.copy(name = value, error = null) }
    }

    fun onNewListDescriptionChange(value: String) {
        _addListUiState.update { it.copy(description = value, error = null) }
    }

    fun createList() {
        // Verificar permiso antes de crear
        if (!sessionManager.canCreateLists()) {
            _addListUiState.update { it.copy(error = "No tienes permiso para crear listas") }
            return
        }

        val roomId = sessionManager.currentRoomId.value
        if (roomId == null) {
            _addListUiState.update { it.copy(error = "No estás en una sala.") }
            return
        }

        val state = _addListUiState.value
        if (state.name.isBlank()) {
            _addListUiState.update { it.copy(error = "El nombre de la lista es obligatorio") }
            return
        }

        viewModelScope.launch {
            _addListUiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                useCases.createSharedList(
                    roomId,
                    CreateListParams(
                        name = state.name,
                        description = state.description,
                        colorHex = state.colorHex
                    )
                )
            }.onSuccess {
                _addListUiState.update {
                    it.copy(
                        isLoading = false,
                        isSaved = true,
                        name = "",
                        description = ""
                    )
                }
                _message.value = "Lista creada exitosamente"
            }.onFailure { throwable ->
                _addListUiState.update {
                    it.copy(isLoading = false, error = throwable.message ?: "Error creating list")
                }
            }
        }
    }

    fun resetCreateListState() {
        _addListUiState.update { it.copy(isSaved = false, error = null) }
    }

    fun onAnnouncementIndexChanged(index: Int) {
        _currentAnnouncementIndex.value = index
    }

    fun openEditModal(list: SharedListItemUiModel) {
        _editModalUiState.value = EditListModalUiState(
            isVisible = true,
            listId = list.id,
            name = list.name,
            description = list.description
        )
    }

    fun closeEditModal() {
        _editModalUiState.value = EditListModalUiState()
    }

    fun onEditNameChange(value: String) {
        _editModalUiState.update { it.copy(name = value, error = null) }
    }

    fun onEditDescriptionChange(value: String) {
        _editModalUiState.update { it.copy(description = value, error = null) }
    }

    fun saveListEdition() {
        val state = _editModalUiState.value
        if (state.name.isBlank()) {
            _editModalUiState.update { it.copy(error = "El nombre no puede estar vacio") }
            return
        }

        viewModelScope.launch {
            _editModalUiState.update { it.copy(isSaving = true, error = null) }
            runCatching {
                useCases.updateSharedList(
                    UpdateListParams(
                        listId = state.listId,
                        name = state.name,
                        description = state.description
                    )
                )
            }.onSuccess {
                closeEditModal()
                loadSharedLists()
                _message.value = "Lista actualizada exitosamente"
            }.onFailure { throwable ->
                _editModalUiState.update {
                    it.copy(isSaving = false, error = throwable.message ?: "Error updating list")
                }
            }
        }
    }

    fun openDeleteModal(list: SharedListItemUiModel) {
        _deleteModalUiState.value = DeleteListModalUiState(
            isVisible = true,
            listId = list.id,
            listName = list.name
        )
    }

    fun closeDeleteModal() {
        _deleteModalUiState.value = DeleteListModalUiState()
    }

    fun deleteList() {
        val state = _deleteModalUiState.value
        viewModelScope.launch {
            _deleteModalUiState.update { it.copy(isDeleting = true, error = null) }
            runCatching { useCases.deleteSharedList(state.listId) }
                .onSuccess {
                    closeDeleteModal()
                    loadSharedLists()
                    _message.value = "Lista eliminada exitosamente"
                }
                .onFailure { throwable ->
                    _deleteModalUiState.update {
                        it.copy(isDeleting = false, error = throwable.message ?: "Error deleting list")
                    }
                }
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    fun clearError() {
        _error.value = null
    }
}
