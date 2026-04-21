package com.alejandra.amordepelis.features.home.data.datasources.local.mapper

import com.alejandra.amordepelis.core.database.entities.MovieEntity
import com.alejandra.amordepelis.features.home.data.datasources.remote.model.MovieDto
import com.alejandra.amordepelis.features.home.domain.entities.Movie

fun MovieEntity.toDomain() = Movie(id.toString(), titulo, sinopsis)

fun MovieEntity.toDto() = MovieDto(id.toString(), titulo, rating, duracion, genre, imageUrl)