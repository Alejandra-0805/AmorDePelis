package com.alejandra.amordepelis.features.home.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class MetricsDto(
    @SerializedName("first_person_name")
    val firstPersonName: String,
    @SerializedName("second_person_name")
    val secondPersonName: String,
    @SerializedName("movies_watched")
    val moviesWatched: Int,
    val favorites: Int,
    @SerializedName("average_rating")
    val averageRating: Double,
    val lists: Int
)

