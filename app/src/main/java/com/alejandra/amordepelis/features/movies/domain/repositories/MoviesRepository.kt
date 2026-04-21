package com.alejandra.amordepelis.features.movies.domain.repositories

import com.alejandra.amordepelis.features.movies.domain.entities.Movie

interface MoviesRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getMovieDetails(id: Int): Movie
    suspend fun searchMovies(title: String): List<Movie>
    suspend fun addMovie(
        title: String,
        synopsis: String?,
        durationMinutes: Int?,
        tags: String?,
        imageFile: java.io.File?
    ): Movie

    suspend fun syncMovies()
}
