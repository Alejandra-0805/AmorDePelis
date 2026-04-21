package com.alejandra.amordepelis.features.movies.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class TagDto(
    val id: Int,
    val name: String
)

data class MovieDto(
    val id: Int,
    val title: String,
    val synopsis: String? = null,
    @SerializedName("durationMinutes")
    val durationMinutes: Int? = null,
    val imageUrl: String? = null,
    val tags: List<TagDto> = emptyList(),
    @SerializedName("averageRating")
    val averageRating: Double? = null,
    @SerializedName("ratingCount")
    val ratingCount: Int = 0,
    @SerializedName("isFavorite")
    val isFavorite: Boolean = false
)
