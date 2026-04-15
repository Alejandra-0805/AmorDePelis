package com.alejandra.amordepelis.features.home.domain.entities

data class Movie(
    val id: String,
    val title: String,
    val imageUrl: String? = null
)
