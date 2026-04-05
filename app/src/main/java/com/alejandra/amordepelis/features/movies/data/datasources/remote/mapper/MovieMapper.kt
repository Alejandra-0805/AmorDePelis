package com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto
import com.alejandra.amordepelis.features.movies.domain.entities.Movie

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        synopsis = synopsis,
        genre = genre,
        durationMinutes = durationMinutes,
        rating = rating,
        isFavorite = isFavorite,
        posterUrl = posterUrl
    )
}
