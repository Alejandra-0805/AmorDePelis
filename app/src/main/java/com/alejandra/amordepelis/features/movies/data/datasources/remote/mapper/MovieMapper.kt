package com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto
import com.alejandra.amordepelis.features.movies.domain.entities.Movie

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id.toString(),
        title = title,
        synopsis = synopsis,
        imageUrl = imageUrl,
        tags = tags.map { it.name },
        durationMinutes = durationMinutes,
        averageRating = averageRating,
        ratingCount = ratingCount,
        isFavorite = isFavorite
    )
}
