package com.alejandra.amordepelis.features.home.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.home.data.datasources.remote.model.NewsDto
import com.alejandra.amordepelis.features.home.domain.entities.Announcement
import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto
import com.alejandra.amordepelis.features.home.domain.entities.Movie

fun NewsDto.toDomain(): Announcement = Announcement(
    id = id.toString(),
    title = title,
    description = content,
    imageUrl = imageUrl
)

fun List<NewsDto>.toAnnouncementDomainList(): List<Announcement> = map { it.toDomain() }

fun MovieDto.toHomeMovie(): Movie = Movie(
    id = id.toString(),
    title = title,
    synopsis = synopsis,
    durationMinutes = durationMinutes,
    imageUrl = imageUrl,
    tags = tags.map { it.name },
    averageRating = averageRating,
    ratingCount = ratingCount,
    isFavorite = isFavorite
)

fun List<MovieDto>.toHomeMovieList(): List<Movie> = map { it.toHomeMovie() }
