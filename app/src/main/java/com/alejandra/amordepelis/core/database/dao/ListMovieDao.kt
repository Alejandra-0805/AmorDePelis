package com.alejandra.amordepelis.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alejandra.amordepelis.core.database.entities.ListMovieEntity

@Dao
interface ListMovieDao {
    @Query("SELECT * FROM lists_movies WHERE listId = :listId")
    fun getMoviesByListId(listId: Int): List<ListMovieEntity>

    @Insert
    fun createListMovie(listMovie: ListMovieEntity)
}