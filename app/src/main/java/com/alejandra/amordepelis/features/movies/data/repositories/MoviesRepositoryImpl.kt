package com.alejandra.amordepelis.features.movies.data.repositories

import com.alejandra.amordepelis.features.movies.data.datasources.remote.api.MoviesApi
import com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper.toDto
import com.alejandra.amordepelis.features.movies.domain.entities.AddMovieParams
import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi
) : MoviesRepository {
    override suspend fun getMovies(): List<Movie> {
        return moviesApi.getMovies().map { it.toDomain() }
    }

    override suspend fun getMovieById(movieId: String): Movie {
        return moviesApi.getMovieById(movieId).toDomain()
    }

    override suspend fun addMovie(params: AddMovieParams): Movie {
        return moviesApi.addMovie(params.toDto()).toDomain()
    }
}
