package com.alejandra.amordepelis.features.movies.domain.entities

data class Movie(
    val id: String,
    val title: String,
    val synopsis: String? = null,
    val imageUrl: String? = null,
    val tags: List<String> = emptyList()
)
