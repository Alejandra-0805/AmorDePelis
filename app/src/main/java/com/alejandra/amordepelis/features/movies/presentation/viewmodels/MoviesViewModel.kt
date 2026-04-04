package com.alejandra.amordepelis.features.movies.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.features.movies.di.MoviesUseCaseModule
import com.alejandra.amordepelis.features.movies.domain.entities.AddMovieParams
import com.alejandra.amordepelis.features.movies.domain.usecases.MoviesUseCases
import com.alejandra.amordepelis.features.movies.presentation.screens.AddMovieUiState
import com.alejandra.amordepelis.features.movies.presentation.screens.MovieDetailsUiState
import com.alejandra.amordepelis.features.movies.presentation.screens.MoviesListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val useCases: MoviesUseCases = MoviesUseCaseModule.provideMoviesUseCases()
) : ViewModel() {

    private val _listUiState = MutableStateFlow(MoviesListUiState())
    val listUiState = _listUiState.asStateFlow()

    private val _detailsUiState = MutableStateFlow(MovieDetailsUiState())
    val detailsUiState = _detailsUiState.asStateFlow()

    private val _addUiState = MutableStateFlow(AddMovieUiState())
    val addUiState = _addUiState.asStateFlow()

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
