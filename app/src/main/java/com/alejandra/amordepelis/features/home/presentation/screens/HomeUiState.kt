package com.alejandra.amordepelis.features.home.presentation.screens

data class HomeUiState(
    val title: String = "Nuestra Cartelera",
    val firstPersonName: String = "Persona 1",
    val secondPersonName: String = "Persona 2",
    val moviesWatched: Int = 0,
    val favorites: Int = 0,
    val averageRating: Double = 0.0,
    val lists: Int = 0,
    val recentMovies: List<RecentMovieUiModel> = emptyList(),
    val addFirstMovieButtonLabel: String = "Agregar primera película",
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

data class RecentMovieUiModel(
    val id: String,
    val title: String,
    val rating: Int,
    val durationMinutes: Int,
    val genre: String
)
