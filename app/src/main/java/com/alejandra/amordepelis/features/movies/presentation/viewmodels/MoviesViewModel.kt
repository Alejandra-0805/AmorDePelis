package com.alejandra.amordepelis.features.movies.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.features.movies.domain.entities.AddMovieParams
import com.alejandra.amordepelis.features.movies.domain.usecases.MoviesUseCases
import com.alejandra.amordepelis.features.movies.presentation.screens.AddMovieUiState
import com.alejandra.amordepelis.features.movies.presentation.screens.Announcement
import com.alejandra.amordepelis.features.movies.presentation.screens.MovieDetailsUiState
import com.alejandra.amordepelis.features.movies.presentation.screens.MoviesListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val useCases: MoviesUseCases
) : ViewModel() {

    private val _listUiState = MutableStateFlow(MoviesListUiState())
    val listUiState: StateFlow<MoviesListUiState> = _listUiState.asStateFlow()

    private val _detailsUiState = MutableStateFlow(MovieDetailsUiState())
    val detailsUiState: StateFlow<MovieDetailsUiState> = _detailsUiState.asStateFlow()

    private val _addUiState = MutableStateFlow(AddMovieUiState())
    val addUiState: StateFlow<AddMovieUiState> = _addUiState.asStateFlow()

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    private val _currentAnnouncementIndex = MutableStateFlow(0)
    val currentAnnouncementIndex: StateFlow<Int> = _currentAnnouncementIndex.asStateFlow()

    init {
        loadAnnouncements()
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

    fun loadMovieDetails(movieId: String) {
        viewModelScope.launch {
            _detailsUiState.update { it.copy(isLoading = true, error = null) }
            runCatching { useCases.getMovieDetails(movieId) }
                .onSuccess { movie ->
                    _detailsUiState.update { it.copy(isLoading = false, movie = movie) }
                }
                .onFailure { throwable ->
                    _detailsUiState.update {
                        it.copy(isLoading = false, error = throwable.message ?: "Error loading movie")
                    }
                }
        }
    }

    fun onTitleChange(value: String) {
        _addUiState.update { it.copy(movieTitle = value, error = null) }
    }

    fun onSynopsisChange(value: String) {
        _addUiState.update { it.copy(synopsis = value, error = null) }
    }

    fun onGenreChange(value: String) {
        _addUiState.update { it.copy(genre = value, error = null) }
    }

    fun onDurationChange(value: String) {
        _addUiState.update { it.copy(durationMinutes = value, error = null) }
    }

    fun onRatingChange(value: Int) {
        _addUiState.update { it.copy(rating = value.coerceIn(0, 5), error = null) }
    }

    fun onFavoriteChange(value: Boolean) {
        _addUiState.update { it.copy(isFavorite = value) }
    }

    fun addMovie() {
        val state = _addUiState.value
        if (state.movieTitle.isBlank()) {
            _addUiState.update { it.copy(error = "Movie title is required") }
            return
        }

        val duration = state.durationMinutes.toIntOrNull() ?: 0

        viewModelScope.launch {
            _addUiState.update { it.copy(isLoading = true, error = null, isSaved = false) }
            runCatching {
                useCases.addMovie(
                    AddMovieParams(
                        title = state.movieTitle,
                        synopsis = state.synopsis,
                        genre = state.genre,
                        durationMinutes = duration,
                        rating = state.rating,
                        isFavorite = state.isFavorite
                    )
                )
            }.onSuccess {
                _addUiState.update {
                    it.copy(
                        isLoading = false,
                        isSaved = true,
                        movieTitle = "",
                        synopsis = "",
                        genre = "",
                        durationMinutes = "",
                        rating = 0,
                        isFavorite = false
                    )
                }
            }.onFailure { throwable ->
                _addUiState.update {
                    it.copy(isLoading = false, error = throwable.message ?: "Error saving movie")
                }
            }
        }
    }

    fun resetAddMovieState() {
        _addUiState.update { it.copy(isSaved = false, error = null) }
    }
}
