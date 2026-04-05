package com.alejandra.amordepelis.features.lists.data.datasources.remote.model

data class CreateListRequestDto(
    val name: String,
    val description: String,
    val colorHex: String
)
