package com.alejandra.amordepelis.features.movies.domain.entities

data class Movie(
    val id: String,
    val title: String,
    val synopsis: String? = null,
    val imageUrl: String? = null,
    val tags: List<String> = emptyList(),
    val durationMinutes: Int? = null,
    val averageRating: Double? = null,
    val ratingCount: Int = 0,
    val isFavorite: Boolean = false
)
