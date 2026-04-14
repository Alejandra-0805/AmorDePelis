package com.alejandra.amordepelis.features.movies.domain.entities

data class AddMovieParams(
    val title: String,
    val synopsis: String,
    val genre: String,
    val durationMinutes: Int,
    val rating: Int,
    val isFavorite: Boolean
)
