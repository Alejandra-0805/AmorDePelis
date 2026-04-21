package com.alejandra.amordepelis.features.movies.data.datasources.local.mapper

import com.alejandra.amordepelis.core.database.entities.MovieEntity
import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto

fun MovieDto.toEntity(): MovieEntity = MovieEntity(
    id = id,
    titulo = title,
    sinopsis = synopsis ?: "",
    genre = tags.joinToString(", ") { it.name },
    rating = averageRating?.toInt() ?: 0,
    duracion = durationMinutes ?: 0,
    imageUrl = imageUrl ?: ""
)

fun List<MovieDto>.toEntityList(): List<MovieEntity> = map { it.toEntity() }
