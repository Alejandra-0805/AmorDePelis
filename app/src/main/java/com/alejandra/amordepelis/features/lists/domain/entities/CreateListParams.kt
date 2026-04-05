package com.alejandra.amordepelis.features.lists.domain.entities

data class CreateListParams(
    val name: String,
    val description: String,
    val colorHex: String
)
