package com.alejandra.amordepelis.features.lists.data.datasources.remote.model

import com.google.gson.annotations.SerializedName

data class CustomListDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
