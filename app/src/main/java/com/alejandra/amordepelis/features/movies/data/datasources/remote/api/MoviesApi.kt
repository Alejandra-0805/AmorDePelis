package com.alejandra.amordepelis.features.movies.data.datasources.remote.api

import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.AddMovieRequestDto
import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto

interface MoviesApi {
    suspend fun getMovies(): List<MovieDto>
    suspend fun getMovieById(movieId: String): MovieDto
    suspend fun addMovie(request: AddMovieRequestDto): MovieDto
}
