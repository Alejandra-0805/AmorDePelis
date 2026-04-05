package com.alejandra.amordepelis.features.lists.domain.entities

data class Announcement(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null
)
