package com.alejandra.amordepelis.features.movies.data.datasources.local.mapper

import com.alejandra.amordepelis.core.database.entities.MovieEntity
import com.alejandra.amordepelis.features.movies.domain.entities.Movie

fun MovieEntity.toDomain() = Movie(
    id = id.toString(),
    title = titulo,
    synopsis = sinopsis,
    imageUrl = imageUrl,
    tags = genre.split(", ").filter { it.isNotEmpty() },
    durationMinutes = duracion,
    averageRating = rating.toDouble(),
    ratingCount = 0,
    isFavorite = false
)

fun List<MovieEntity>.toDomainList(): List<Movie> = map { it.toDomain() }
