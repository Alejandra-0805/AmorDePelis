package com.alejandra.amordepelis.features.movies.domain.entities

data class Movie(
    val id: String,
    val title: String,
    val synopsis: String,
    val genre: String,
    val durationMinutes: Int,
    val rating: Int,
    val isFavorite: Boolean,
    val posterUrl: String? = null
)
