package com.alejandra.amordepelis.features.lists.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.core.storage.SessionManager
import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.usecases.ListsUseCases
import com.alejandra.amordepelis.features.lists.presentation.screens.AddListUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.AnnouncementUiModel
import com.alejandra.amordepelis.features.lists.presentation.screens.ListDetailsUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.ListsScreenUiState
import com.alejandra.amordepelis.features.lists.presentation.screens.SharedListItemUiModel
import com.alejandra.amordepelis.features.user.data.datasources.remote.api.UserApi
import com.alejandra.amordepelis.core.hardware.domain.HapticFeedbackManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(
    private val useCases: ListsUseCases,
    private val sessionManager: SessionManager,
    private val userApi: UserApi,
    private val hapticFeedbackManager: HapticFeedbackManager
) : ViewModel() {

    private val _listsUiState = MutableStateFlow(ListsScreenUiState())
    val listsUiState: StateFlow<ListsScreenUiState> = _listsUiState.asStateFlow()

    private val _addListUiState = MutableStateFlow(AddListUiState())
    val addListUiState: StateFlow<AddListUiState> = _addListUiState.asStateFlow()

    private val _detailsUiState = MutableStateFlow(ListDetailsUiState())
    val detailsUiState: StateFlow<ListDetailsUiState> = _detailsUiState.asStateFlow()

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

    // Estado del selector de lista para agregar una película
    private val _movieIdPendingAdd = MutableStateFlow<String?>(null)
    val movieIdPendingAdd: StateFlow<String?> = _movieIdPendingAdd.asStateFlow()

    private val _isAddingMovieToList = MutableStateFlow(false)
    val isAddingMovieToList: StateFlow<Boolean> = _isAddingMovieToList.asStateFlow()

    // IDs de las listas que YA contienen la película pendiente de agregar
    private val _listIdsContainingPendingMovie = MutableStateFlow<Set<String>>(emptySet())
    val listIdsContainingPendingMovie: StateFlow<Set<String>> = _listIdsContainingPendingMovie.asStateFlow()

    // Almacena el roomId activo del usuario
    private var activeRoomId: Int? = null

    init {
        updatePermissions()
        loadActiveRoom()
    }

    private fun updatePermissions() {
        viewModelScope.launch {
            sessionManager.currentRole.collect { role ->
                _listsUiState.update { 
                    it.copy(
                        canCreateLists = role.canCreateLists(),
                        canEditLists = false,  // No existe endpoint de editar listas
                        canDeleteLists = false  // No existe endpoint de eliminar listas
                    )
                }
            }
        }
    }

    private fun loadActiveRoom() {
        viewModelScope.launch {
            try {
                val rooms = userApi.getUserRooms()
                if (rooms.isNotEmpty()) {
                    activeRoomId = rooms.first().id
                    loadSharedLists()
                }
            } catch (e: Exception) {
                _error.value = "No se pudo cargar la sala activa"
            }
        }
    }

    // Funciones de permisos para la UI
    fun canCreateLists(): Boolean = sessionManager.canCreateLists()

    fun loadSharedLists() {
        val roomId = activeRoomId ?: return
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
                    // El backend no devuelve movieCount; lo calculamos en cliente
                    // pidiendo las películas de cada lista en paralelo.
                    enrichListsWithMovieCounts(roomId, lists.map { it.id })
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

    fun loadListDetails(listId: String, listName: String = "") {
        val roomId = activeRoomId ?: return
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.getSharedListDetails(roomId, listId.toInt(), listName) }
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
            _error.value = "No tienes permiso para crear listas"
            return
        }

        val roomId = activeRoomId
        if (roomId == null) {
            _error.value = "No se encontró una sala activa. Únete o crea una sala primero."
            return
        }

        val state = _addListUiState.value
        if (state.name.isBlank()) {
            _error.value = "El nombre de la lista es obligatorio"
            return
        }

        viewModelScope.launch {
            _addListUiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                useCases.createSharedList(
                    CreateListParams(
                        roomId = roomId,
                        name = state.name
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
                hapticFeedbackManager.vibrateForNotification()
                loadSharedLists()
            }.onFailure { throwable ->
                _addListUiState.update { it.copy(isLoading = false) }
                _error.value = throwable.message ?: "Error creating list"
            }
        }
    }

    fun requestAddMovieToList(movieId: String) {
        val roomId = activeRoomId
        if (roomId == null) {
            _error.value = "No se encontró una sala activa. Únete o crea una sala primero."
            return
        }
        if (_listsUiState.value.lists.isEmpty()) {
            // Intenta recargar por si aún no se habían cargado, y avisa si sigue vacío
            loadSharedLists()
            if (_listsUiState.value.lists.isEmpty()) {
                _error.value = "No tienes listas disponibles. Crea una lista primero."
                return
            }
        }
        _movieIdPendingAdd.value = movieId
        _listIdsContainingPendingMovie.value = emptySet()
        computeListsContainingMovie(roomId, movieId)
    }

    fun dismissAddMovieToList() {
        if (_isAddingMovieToList.value) return
        _movieIdPendingAdd.value = null
        _listIdsContainingPendingMovie.value = emptySet()
    }

    private fun computeListsContainingMovie(roomId: Int, movieId: String) {
        val targetMovieId = movieId
        val lists = _listsUiState.value.lists
        viewModelScope.launch {
            runCatching {
                coroutineScope {
                    lists.map { list ->
                        async {
                            val contains = runCatching {
                                useCases.getSharedListDetails(
                                    roomId,
                                    list.id.toInt(),
                                    list.name
                                ).movies.any { it.id == targetMovieId }
                            }.getOrDefault(false)
                            if (contains) list.id else null
                        }
                    }.awaitAll().filterNotNull().toSet()
                }
            }.onSuccess { ids ->
                // Solo aplicamos si sigue siendo la misma película pendiente
                if (_movieIdPendingAdd.value == targetMovieId) {
                    _listIdsContainingPendingMovie.value = ids
                }
            }
        }
    }

    private fun enrichListsWithMovieCounts(roomId: Int, listIds: List<String>) {
        if (listIds.isEmpty()) return
        viewModelScope.launch {
            runCatching {
                coroutineScope {
                    listIds.map { listId ->
                        async {
                            val count = runCatching {
                                useCases.getSharedListDetails(
                                    roomId,
                                    listId.toInt(),
                                    ""
                                ).movies.size
                            }.getOrNull()
                            listId to count
                        }
                    }.awaitAll().toMap()
                }
            }.onSuccess { counts ->
                _listsUiState.update { state ->
                    state.copy(
                        lists = state.lists.map { item ->
                            val newCount = counts[item.id]
                            if (newCount != null) item.copy(movieCount = newCount) else item
                        }
                    )
                }
            }
        }
    }

    fun confirmAddMovieToList(listId: String) {
        val roomId = activeRoomId ?: return
        val movieId = _movieIdPendingAdd.value ?: return

        viewModelScope.launch {
            _isAddingMovieToList.value = true
            runCatching {
                useCases.addMovieToList(roomId, listId.toInt(), movieId.toInt())
            }.onSuccess {
                _message.value = "¡Agregado a la lista exitosamente!"
                hapticFeedbackManager.vibrateForNotification()
                _movieIdPendingAdd.value = null
                loadSharedLists()
            }.onFailure { throwable ->
                _error.value = throwable.message
                    ?: "No se pudo agregar a la lista. Puede que ya esté en ella."
                _movieIdPendingAdd.value = null
            }
            _isAddingMovieToList.value = false
        }
    }

    fun resetCreateListState() {
        _addListUiState.update { it.copy(isSaved = false, error = null) }
    }

    fun onAnnouncementIndexChanged(index: Int) {
        _currentAnnouncementIndex.value = index
    }

    fun clearMessage() {
        _message.value = null
    }

    fun clearError() {
        _error.value = null
    }
}
