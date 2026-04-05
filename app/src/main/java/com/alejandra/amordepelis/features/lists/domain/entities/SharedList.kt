package com.alejandra.amordepelis.features.lists.domain.entities

data class SharedList(
    val id: String,
    val name: String,
    val description: String,
    val colorHex: String,
    val movieCount: Int
)
