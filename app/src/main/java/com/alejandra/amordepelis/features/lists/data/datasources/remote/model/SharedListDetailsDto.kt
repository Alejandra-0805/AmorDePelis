package com.alejandra.amordepelis.features.lists.data.datasources.remote.model

import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto

data class SharedListDetailsDto(
    val id: String,
    val name: String,
    val description: String,
    val colorHex: String,
    val movies: List<MovieDto>
)
