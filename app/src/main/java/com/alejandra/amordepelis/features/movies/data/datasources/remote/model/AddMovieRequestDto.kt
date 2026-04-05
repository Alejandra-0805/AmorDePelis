package com.alejandra.amordepelis.features.movies.data.datasources.remote.model

data class AddMovieRequestDto(
    val title: String,
    val synopsis: String,
    val genre: String,
    val durationMinutes: Int,
    val rating: Int,
    val isFavorite: Boolean
)
