package com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.AddMovieRequestDto
import com.alejandra.amordepelis.features.movies.domain.entities.AddMovieParams

fun AddMovieParams.toDto(): AddMovieRequestDto {
    return AddMovieRequestDto(
        title = title,
        synopsis = synopsis,
        genre = genre,
        durationMinutes = durationMinutes,
        rating = rating,
        isFavorite = isFavorite
    )
}
