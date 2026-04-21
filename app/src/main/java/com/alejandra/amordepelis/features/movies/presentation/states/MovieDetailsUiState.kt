package com.alejandra.amordepelis.features.movies.presentation.states

import com.alejandra.amordepelis.features.movies.domain.entities.Movie

data class MovieDetailsUiState(
    val movie: Movie? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
