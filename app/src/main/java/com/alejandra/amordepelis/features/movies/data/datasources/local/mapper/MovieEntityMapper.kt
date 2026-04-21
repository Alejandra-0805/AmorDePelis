package com.alejandra.amordepelis.features.movies.data.datasources.local.mapper

import com.alejandra.amordepelis.core.database.entities.MovieEntity
import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto
import com.alejandra.amordepelis.features.movies.domain.entities.Movie

fun MovieEntity.toDomain() = Movie(id.toString(), titulo, sinopsis, imageUrl)

fun MovieEntity.toDto() = MovieDto(id, titulo, sinopsis, duracion, imageUrl)