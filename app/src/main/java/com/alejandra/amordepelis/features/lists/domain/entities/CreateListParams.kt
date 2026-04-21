package com.alejandra.amordepelis.features.lists.domain.entities

data class CreateListParams(
    val roomId: Int,
    val name: String,
    val description: String = "",
    val colorHex: String = "#3B82F6"
)
