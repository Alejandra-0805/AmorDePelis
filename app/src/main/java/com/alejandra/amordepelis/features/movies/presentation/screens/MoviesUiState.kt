package com.alejandra.amordepelis.features.movies.presentation.screens

import com.alejandra.amordepelis.features.movies.domain.entities.Movie

data class MoviesListUiState(
    val title: String = "Peliculas",
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)

data class MovieDetailsUiState(
    val title: String = "Detalles de pelicula",
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: String? = null
)

data class AddMovieUiState(
    val title: String = "Agregar pelicula",
    val movieTitle: String = "",
    val synopsis: String = "",
    val genre: String = "",
    val durationMinutes: String = "",
    val rating: Int = 0,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

data class Announcement(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String
)
