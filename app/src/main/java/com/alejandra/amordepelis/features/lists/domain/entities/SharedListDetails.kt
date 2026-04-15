package com.alejandra.amordepelis.features.lists.domain.entities

import com.alejandra.amordepelis.features.movies.domain.entities.Movie

data class SharedListDetails(
    val id: String,
    val name: String,
    val description: String = "",
    val colorHex: String = "#E91E63",
    val movies: List<Movie>
)
