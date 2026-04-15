package com.alejandra.amordepelis.features.home.data.datasources.remote.model

data class NewsDto(
    val id: Int,
    val title: String,
    val content: String,
    val publishDate: String,
    val imageUrl: String? = null
)
