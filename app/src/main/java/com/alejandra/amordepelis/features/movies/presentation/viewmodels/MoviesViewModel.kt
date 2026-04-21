package com.alejandra.amordepelis.features.movies.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.core.hardware.domain.AccelerometerManager
import com.alejandra.amordepelis.core.hardware.domain.CameraManager
import com.alejandra.amordepelis.core.hardware.domain.HapticFeedbackManager
import com.alejandra.amordepelis.core.network.connectivity.ConnectivityManager
import com.alejandra.amordepelis.core.storage.SessionManager
import com.alejandra.amordepelis.core.storage.UserRole
import com.alejandra.amordepelis.features.movies.domain.usecases.MoviesUseCases
import com.alejandra.amordepelis.features.movies.presentation.screens.AddMovieUiState
import com.alejandra.amordepelis.features.movies.presentation.screens.Announcement
import com.alejandra.amordepelis.features.movies.presentation.screens.MovieDetailsUiState
import com.alejandra.amordepelis.features.movies.presentation.screens.MoviesListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val useCases: MoviesUseCases,
    private val sessionManager: SessionManager,
    private val connectivityManager: ConnectivityManager,
    @ApplicationContext private val context: Context,
    private val hapticFeedbackManager: HapticFeedbackManager,
    private val cameraManager: CameraManager,
    val accelerometerManager: AccelerometerManager
) : ViewModel() {

    companion object {
        private const val TAG = "MoviesViewModel"
    }

    private val _listUiState = MutableStateFlow(MoviesListUiState())
    val listUiState: StateFlow<MoviesListUiState> = _listUiState.asStateFlow()

    private val _detailsUiState = MutableStateFlow(MovieDetailsUiState())
    val detailsUiState: StateFlow<MovieDetailsUiState> = _detailsUiState.asStateFlow()

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    private val _currentAnnouncementIndex = MutableStateFlow(0)
    val currentAnnouncementIndex: StateFlow<Int> = _currentAnnouncementIndex.asStateFlow()

    private val _addMovieUiState = MutableStateFlow(AddMovieUiState())
    val addMovieUiState: StateFlow<AddMovieUiState> = _addMovieUiState.asStateFlow()

    private var syncJob: Job? = null

    val currentRole: StateFlow<UserRole> = sessionManager.currentRole

    init {
        observeMoviesStream()
        observeConnectivity()
        observePermissions()
        loadAnnouncements()
    }

    private fun observeMoviesStream() {
        viewModelScope.launch {
            _listUiState.update { it.copy(isLoading = true) }

            useCases.getMovies().collect { movies ->
                val isOffline = !connectivityManager.isNetworkAvailable()
                _listUiState.update { state ->
                    state.copy(
                        isLoading = false,
                        movies = movies,
                        hasOfflineData = isOffline && movies.isNotEmpty(),
                        error = if (movies.isNotEmpty()) null else state.error
                    )
                }
                Log.d(TAG, "Flow emitió ${movies.size} películas (offline=$isOffline)")
            }
        }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityManager.isConnected.collect { isConnected ->
                _listUiState.update { it.copy(isConnected = isConnected) }

                if (isConnected) {
                    Log.d(TAG, "Red detectada. Iniciando sincronización en background.")
                    triggerBackgroundSync()
                } else {
                    Log.d(TAG, "Sin red. Mostrando datos cacheados en Room.")
                    // Cancelar sync en progreso si se pierde la red.
                    syncJob?.cancel()
                    _listUiState.update { it.copy(isSyncing = false) }
                }
            }
        }
    }

    private fun triggerBackgroundSync() {
        syncJob?.cancel()
        syncJob = viewModelScope.launch {
            _listUiState.update { it.copy(isSyncing = true, syncError = null) }

            useCases.syncMovies().onFailure { error ->
                Log.w(TAG, "Sincronización fallida. Usando datos locales.", error)
                _listUiState.update {
                    it.copy(
                        isSyncing = false,
                        syncError = "No se pudo sincronizar. Mostrando datos guardados."
                    )
                }
            }.onSuccess {
                Log.d(TAG, "Sincronización exitosa.")
                _listUiState.update { it.copy(isSyncing = false, syncError = null) }
            }
        }
    }

    fun refreshMovies() {
        if (connectivityManager.isNetworkAvailable()) {
            triggerBackgroundSync()
        } else {
            _listUiState.update {
                it.copy(syncError = "Sin conexión. Mostrando películas guardadas.")
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
                        it.copy(isLoading = false, error = throwable.message ?: "Error al buscar")
                    }
                }
        }
    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.getMovieDetails(movieId) }
                .onSuccess { movie ->
                    _detailsUiState.update { it.copy(isLoading = false, movie = movie) }
                }
                .onFailure { throwable ->
                    _detailsUiState.update {
                        it.copy(isLoading = false, error = throwable.message ?: "Error al cargar detalles")
                    }
                }
        }
    }

    private fun observePermissions() {
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

    fun canAddMoviesToCatalog(): Boolean = sessionManager.canAddMoviesToCatalog()
    fun canAddToPersonalLists(): Boolean = sessionManager.canAddMoviesToPersonalLists()
    fun canMarkAsWatched(): Boolean = sessionManager.canMarkMoviesAsWatched()
    fun canMarkAsFavorite(): Boolean = sessionManager.canMarkMoviesAsFavorite()

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

    fun updateAddMovieTitle(title: String) {
        _addMovieUiState.update { it.copy(movieTitle = title) }
    }

    fun updateAddMovieSynopsis(synopsis: String) {
        _addMovieUiState.update { it.copy(synopsis = synopsis) }
    }

    fun updateAddMovieImageUri(uri: String?) {
        // El AddMovieUiState no tiene imageUri; manejar en la pantalla si se necesita.
    }

    // ── Cámara ─────────────────────────────────────────────────────────────────

    /** Crea un URI temporal para la cámara. Devuelve null si FileProvider falla. */
    fun createCameraUri(): Uri? = cameraManager.createTempCameraUri()

    /** Comprueba si el permiso de cámara está concedido. */
    fun hasCameraPermission(): Boolean = cameraManager.hasCameraPermission()

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

    fun clearSyncError() {
        _listUiState.update { it.copy(syncError = null) }
    }

    fun addMovie(imageUriString: String? = null) {
        val state = _addMovieUiState.value

        if (state.movieTitle.isBlank()) {
            _addMovieUiState.update { it.copy(error = "El título es obligatorio") }
            return
        }

        viewModelScope.launch {
            _addMovieUiState.update { it.copy(isLoading = true, error = null) }

            var tempFile: File? = null
            if (imageUriString != null) {
                try {
                    val uri = Uri.parse(imageUriString)
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
                    title = state.movieTitle,
                    synopsis = state.synopsis.takeIf { it.isNotBlank() },
                    durationMinutes = state.durationMinutes.toIntOrNull(),
                    tags = state.genre.takeIf { it.isNotBlank() },
                    imageFile = tempFile
                )
            }.onSuccess {
                // Room notificará el Flow automáticamente; no hace falta loadMovies().
                _addMovieUiState.update { it.copy(isLoading = false, isSaved = true) }
                tempFile?.delete()
                hapticFeedbackManager.vibrateForNotification()
            }.onFailure { throwable ->
                _addMovieUiState.update {
                    it.copy(isLoading = false, error = throwable.message ?: "Error al subir película")
                }
                tempFile?.delete()
            }
        }
    }
}
