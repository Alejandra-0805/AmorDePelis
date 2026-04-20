package com.alejandra.amordepelis.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.alejandra.amordepelis.core.database.entities.MovieEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): MovieEntity?

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<MovieEntity>

}