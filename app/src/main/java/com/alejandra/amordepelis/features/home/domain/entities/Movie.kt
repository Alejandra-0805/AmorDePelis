package com.alejandra.amordepelis.features.home.domain.entities

data class Movie(
    val id: String,
    val title: String,
    val rating: Int,
    val durationMinutes: Int,
    val genre: String,
    val posterUrl: String? = null
)

