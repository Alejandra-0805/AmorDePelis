package com.alejandra.amordepelis.features.movies.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.core.storage.SessionManager
import com.alejandra.amordepelis.core.storage.UserRole
import com.alejandra.amordepelis.features.movies.domain.usecases.MoviesUseCases
import com.alejandra.amordepelis.features.movies.presentation.screens.Announcement
import com.alejandra.amordepelis.features.movies.presentation.screens.MoviesListUiState
import com.alejandra.amordepelis.core.hardware.domain.HapticFeedbackManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class AddMovieUiState(
    val title: String = "",
    val synopsis: String = "",
    val imageUri: String? = null,
    val rating: Int = 0,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val useCases: MoviesUseCases,
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context,
    private val hapticFeedbackManager: HapticFeedbackManager
) : ViewModel() {

    private val _listUiState = MutableStateFlow(MoviesListUiState())
    val listUiState: StateFlow<MoviesListUiState> = _listUiState.asStateFlow()

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    private val _currentAnnouncementIndex = MutableStateFlow(0)
    val currentAnnouncementIndex: StateFlow<Int> = _currentAnnouncementIndex.asStateFlow()

    private val _addMovieUiState = MutableStateFlow(AddMovieUiState())
    val addMovieUiState: StateFlow<AddMovieUiState> = _addMovieUiState.asStateFlow()

    // Permisos basados en rol
    val currentRole: StateFlow<UserRole> = sessionManager.currentRole

    init {
        loadAnnouncements()
        updatePermissions()
    }

    private fun updatePermissions() {
        viewModelScope.launch {
            sessionManager.currentRole.collect { role ->
                _listUiState.update { 
                    it.copy(
                        canAddMoviesToCatalog = role.canAddMoviesToCatalog(),
                        canAddToPersonalLists = role.canAddMoviesToPersonalLists(),
                        canMarkAsWatched = role.canMarkMoviesAsWatched(),
                        canMarkAsFavorite = role.canMarkMoviesAsFavorite()
                    )
                }
            }
        }
    }

    private fun loadAnnouncements() {
        _announcements.value = listOf(
            Announcement(
                id = "1",
                title = "¡Nuevos estrenos!",
                description = "Descubre las películas más recientes de la cartelera",
                imageUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=800"
            ),
            Announcement(
                id = "2",
                title = "Ofertas especiales",
                description = "2x1 en películas románticas este fin de semana",
                imageUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=800"
            ),
            Announcement(
                id = "3",
                title = "Maratón de cine",
                description = "Prepárate para la maratón de películas de acción",
                imageUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=800"
            )
        )
    }

    fun onAnnouncementIndexChange(index: Int) {
        _currentAnnouncementIndex.value = index
    }

    fun loadMovies() {
        viewModelScope.launch {
            _listUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.getMovies() }
                .onSuccess { movies ->
                    _listUiState.update { it.copy(isLoading = false, movies = movies) }
                }
                .onFailure { throwable ->
                    _listUiState.update {
                        it.copy(isLoading = false, error = throwable.message ?: "Error loading movies")
                    }
                }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _listUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.searchMovies(query) }
                .onSuccess { movies ->
                    _listUiState.update { it.copy(isLoading = false, movies = movies) }
                }
                .onFailure { throwable ->
                    _listUiState.update {
                        it.copy(isLoading = false, error = throwable.message ?: "Error searching movies")
                    }
                }
        }
    }

    // Funciones de permisos para la UI
    fun canAddMoviesToCatalog(): Boolean = sessionManager.canAddMoviesToCatalog()
    fun canAddToPersonalLists(): Boolean = sessionManager.canAddMoviesToPersonalLists()
    fun canMarkAsWatched(): Boolean = sessionManager.canMarkMoviesAsWatched()
    fun canMarkAsFavorite(): Boolean = sessionManager.canMarkMoviesAsFavorite()

    // Add Movie UI Actions
    fun updateAddMovieTitle(title: String) {
        _addMovieUiState.update { it.copy(title = title) }
    }

    fun updateAddMovieSynopsis(synopsis: String) {
        _addMovieUiState.update { it.copy(synopsis = synopsis) }
    }

    fun updateAddMovieImageUri(uri: String?) {
        _addMovieUiState.update { it.copy(imageUri = uri) }
    }

    fun updateAddMovieRating(rating: Int) {
        _addMovieUiState.update { it.copy(rating = rating) }
    }

    fun updateAddMovieIsFavorite(isFavorite: Boolean) {
        _addMovieUiState.update { it.copy(isFavorite = isFavorite) }
    }

    fun resetAddMovieState() {
        _addMovieUiState.value = AddMovieUiState()
    }

    fun clearAddMovieError() {
        _addMovieUiState.update { it.copy(error = null) }
    }

    fun addMovie() {
        val state = _addMovieUiState.value
        
        if (state.title.isBlank()) {
            _addMovieUiState.update { it.copy(error = "El título es obligatorio") }
            return
        }

        viewModelScope.launch {
            _addMovieUiState.update { it.copy(isLoading = true, error = null) }
            
            var tempFile: File? = null
            if (state.imageUri != null) {
                try {
                    val uri = Uri.parse(state.imageUri)
                    val inputStream = context.contentResolver.openInputStream(uri)
                    if (inputStream != null) {
                        tempFile = File(context.cacheDir, "upload_movie_${System.currentTimeMillis()}.jpg")
                        val outputStream = FileOutputStream(tempFile)
                        inputStream.copyTo(outputStream)
                        inputStream.close()
                        outputStream.close()
                    }
                } catch (e: Exception) {
                    _addMovieUiState.update { it.copy(isLoading = false, error = "Error al procesar la imagen") }
                    return@launch
                }
            }

            runCatching {
                useCases.addMovie(
                    title = state.title,
                    synopsis = state.synopsis.takeIf { it.isNotBlank() },
                    durationMinutes = null,
                    tags = null,
                    imageFile = tempFile
                )
            }.onSuccess {
                _addMovieUiState.update { it.copy(isLoading = false, isSaved = true) }
                tempFile?.delete()
                hapticFeedbackManager.vibrateForNotification()
                loadMovies() // Refrescar cartelera global
            }.onFailure { throwable ->
                _addMovieUiState.update { it.copy(isLoading = false, error = throwable.message ?: "Error al subir película") }
                tempFile?.delete()
            }
        }
    }
}
