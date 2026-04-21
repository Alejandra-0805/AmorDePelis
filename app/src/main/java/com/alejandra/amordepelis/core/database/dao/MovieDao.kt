package com.alejandra.amordepelis.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.alejandra.amordepelis.core.database.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies")
    fun observeAllMovies(): Flow<List<MovieEntity>>

    @Upsert
    suspend fun insertMovie(movie: MovieEntity)

    @Upsert
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()
}