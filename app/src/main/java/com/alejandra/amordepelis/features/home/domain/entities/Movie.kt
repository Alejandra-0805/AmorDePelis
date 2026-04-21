package com.alejandra.amordepelis.features.home.domain.entities

data class Movie(
    val id: String,
    val title: String,
    val synopsis: String? = null,
    val durationMinutes: Int? = null,
    val imageUrl: String? = null,
    val tags: List<String> = emptyList(),
    val averageRating: Double? = null,
    val ratingCount: Int = 0,
    val isFavorite: Boolean = false
)
