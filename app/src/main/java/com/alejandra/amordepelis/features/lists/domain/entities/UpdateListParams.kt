package com.alejandra.amordepelis.features.lists.domain.entities

data class UpdateListParams(
    val listId: String,
    val name: String,
    val description: String
)
