package com.alejandra.amordepelis.features.lists.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class AnnouncementDto(
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String? = null
)
