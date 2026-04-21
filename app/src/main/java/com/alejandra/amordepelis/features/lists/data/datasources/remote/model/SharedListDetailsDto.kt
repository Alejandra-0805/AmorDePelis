package com.alejandra.amordepelis.features.lists.data.datasources.remote.model

import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto

data class SharedListDetailsDto(
    val id: Int,
    val name: String,
    val description: String,
    val colorHex: String,
    val movieCount: Int,
    val movies: List<MovieDto>
)
