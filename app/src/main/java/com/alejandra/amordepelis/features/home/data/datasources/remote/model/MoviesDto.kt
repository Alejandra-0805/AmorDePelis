package com.alejandra.amordepelis.features.home.data.datasources.remote.model

//Hacer una clase movie y que movies sea un string de ellas

data class MoviesDto(
    val id: String,
    val title: String,
    val description: String,
    val releaseDate: String,
)
