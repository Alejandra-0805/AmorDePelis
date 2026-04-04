package com.alejandra.amordepelis.features.lists.data.datasources.remote.model

data class UpdateListRequestDto(
    val listId: String,
    val name: String,
    val description: String
)
