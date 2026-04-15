package com.alejandra.amordepelis.features.movies.data.datasources.remote.model

data class TagDto(
    val id: Int,
    val name: String
)

data class MovieDto(
    val id: Int,
    val title: String,
    val synopsis: String? = null,
    val imageUrl: String? = null,
    val tags: List<TagDto> = emptyList()
)
