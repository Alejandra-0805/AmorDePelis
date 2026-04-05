package com.alejandra.amordepelis.features.home.domain.entities

data class Metrics(
    val firstPersonName: String,
    val secondPersonName: String,
    val moviesWatched: Int,
    val favorites: Int,
    val averageRating: Double,
    val lists: Int
)
