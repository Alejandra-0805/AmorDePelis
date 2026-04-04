package com.alejandra.amordepelis.features.lists.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.features.lists.di.ListsUseCaseModule
import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.entities.UpdateListParams
import com.alejandra.amordepelis.features.lists.domain.usecases.ListsUseCases
import com.alejandra.amordepelis.features.lists.presentation.screens.AddListUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.DeleteListModalUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.EditListModalUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.ListDetailsUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.ListsScreenUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.SharedListItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListsViewModel(
    private val useCases: ListsUseCases = ListsUseCaseModule.provideListsUseCases()
) : ViewModel() {

    private val _listsUiState = MutableStateFlow(ListsScreenUiState())
    val listsUiState = _listsUiState.asStateFlow()

    private val _addListUiState = MutableStateFlow(AddListUiState())
    val addListUiState = _addListUiState.asStateFlow()

    private val _detailsUiState = MutableStateFlow(ListDetailsUiState())
    val detailsUiState = _detailsUiState.asStateFlow()

    private val _editModalUiState = MutableStateFlow(EditListModalUiState())
    val editModalUiState = _editModalUiState.asStateFlow()

    private val _deleteModalUiState = MutableStateFlow(DeleteListModalUiState())
    val deleteModalUiState = _deleteModalUiState.asStateFlow()

    fun loadSharedLists() {
        viewModelScope.launch {
            _listsUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.getSharedLists() }
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
                }
                .onFailure { throwable ->
                    _listsUiState.update {
                        it.copy(isLoading = false, error = throwable.message ?: "Error loading lists")
                    }
                }
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
        val state = _addListUiState.value
        if (state.name.isBlank()) {
            _addListUiState.update { it.copy(error = "El nombre de la lista es obligatorio") }
            return
        }

        viewModelScope.launch {
            _addListUiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                useCases.createSharedList(
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
                }
                .onFailure { throwable ->
                    _deleteModalUiState.update {
                        it.copy(isDeleting = false, error = throwable.message ?: "Error deleting list")
                    }
                }
        }
    }
}
