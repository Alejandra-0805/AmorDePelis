package com.alejandra.amordepelis.features.home.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class MovieDto(
    val id: String,
    val title: String,
    val rating: Int,
    @SerializedName("duration_minutes")
    val durationMinutes: Int,
    val genre: String,
    @SerializedName("poster_url")
    val posterUrl: String? = null
)
