package com.alejandra.amordepelis.features.home.data.datasources.remote.mapper

import com.alejandra.amordepelis.features.home.data.datasources.remote.model.AnnouncementDto
import com.alejandra.amordepelis.features.home.data.datasources.remote.model.MetricsDto
import com.alejandra.amordepelis.features.home.data.datasources.remote.model.MovieDto
import com.alejandra.amordepelis.features.home.domain.entities.Announcement
import com.alejandra.amordepelis.features.home.domain.entities.Metrics
import com.alejandra.amordepelis.features.home.domain.entities.Movie

fun MetricsDto.toDomain(): Metrics = Metrics(
    firstPersonName = firstPersonName,
    secondPersonName = secondPersonName,
    moviesWatched = moviesWatched,
    favorites = favorites,
    averageRating = averageRating,
    lists = lists
)

fun MovieDto.toDomain(): Movie = Movie(
    id = id,
    title = title,
    rating = rating,
    durationMinutes = durationMinutes,
    genre = genre,
    posterUrl = posterUrl
)

fun List<MovieDto>.toDomainList(): List<Movie> = map { it.toDomain() }

fun AnnouncementDto.toDomain(): Announcement = Announcement(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl
)

fun List<AnnouncementDto>.toAnnouncementDomainList(): List<Announcement> = map { it.toDomain() }
