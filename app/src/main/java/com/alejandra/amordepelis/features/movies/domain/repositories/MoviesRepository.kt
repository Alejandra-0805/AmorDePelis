package com.alejandra.amordepelis.features.movies.domain.repositories

import com.alejandra.amordepelis.features.movies.domain.entities.AddMovieParams
import com.alejandra.amordepelis.features.movies.domain.entities.Movie

interface MoviesRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun getMovieById(movieId: String): Movie
    suspend fun addMovie(params: AddMovieParams): Movie
}
