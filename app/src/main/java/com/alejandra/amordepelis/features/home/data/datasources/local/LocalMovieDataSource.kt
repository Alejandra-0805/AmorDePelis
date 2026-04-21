package com.alejandra.amordepelis.features.home.data.datasources.local

import com.alejandra.amordepelis.core.database.dao.MovieDao
import com.alejandra.amordepelis.core.database.entities.MovieEntity
import com.alejandra.amordepelis.features.home.data.datasources.local.mapper.toDomain
import com.alejandra.amordepelis.features.home.data.datasources.local.mapper.toDomainList
import com.alejandra.amordepelis.features.home.domain.entities.Movie
import javax.inject.Inject

class LocalMovieDataSource @Inject constructor(
    private val movieDao: MovieDao
) {

    suspend fun getAllMovies(): List<Movie> {
        return movieDao.getAllMovies().toDomainList()
    }

    suspend fun getMovieById(id: Int): Movie? {
        return movieDao.getMovieById(id)?.toDomain()
    }

    suspend fun saveMovie(movie: MovieEntity) {
        movieDao.insertMovie(movie)
    }

    suspend fun saveMovies(movies: List<MovieEntity>) {
        movieDao.insertMovies(movies)
    }

    suspend fun clearAllMovies() {
        movieDao.clearAllMovies()
    }

    suspend fun replaceAllMovies(movies: List<MovieEntity>) {
        clearAllMovies()
        saveMovies(movies)
    }
}
