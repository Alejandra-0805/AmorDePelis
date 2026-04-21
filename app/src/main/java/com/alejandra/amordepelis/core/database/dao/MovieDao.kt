package com.alejandra.amordepelis.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.alejandra.amordepelis.core.database.entities.MovieEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): MovieEntity?

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<MovieEntity>

    @Upsert
    fun insertMovie(movie: MovieEntity)

    @Upsert
    fun insertMovies(movies: List<MovieEntity>)

}