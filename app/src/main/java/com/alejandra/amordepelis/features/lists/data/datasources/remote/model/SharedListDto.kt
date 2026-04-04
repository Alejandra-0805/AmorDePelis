package com.alejandra.amordepelis.features.lists.data.datasources.remote.model

data class SharedListDto(
    val id: String,
    val name: String,
    val description: String,
    val colorHex: String,
    val movieCount: Int
)
