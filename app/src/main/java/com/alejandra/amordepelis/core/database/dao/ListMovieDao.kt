package com.alejandra.amordepelis.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.alejandra.amordepelis.core.database.entities.ListMovieEntity

@Dao
interface ListMovieDao {
    @Query("SELECT * FROM lists_movies WHERE listId = :listId")
    fun getMoviesByListId(listId: Int): List<ListMovieEntity>

    @Query("INSERT INTO lists_movies (listId, movieId) VALUES (:listId, :movieId")
    fun createListMovie(listId: Int, movieId: Int)
}