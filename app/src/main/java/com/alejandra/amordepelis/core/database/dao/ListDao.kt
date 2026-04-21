package com.alejandra.amordepelis.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alejandra.amordepelis.core.database.entities.ListEntity

@Dao
interface ListDao {
    @Query("SELECT * FROM lists WHERE sala_id = :salaId")
    suspend fun getListsBySalaId(salaId: Int): List<ListEntity>

    @Query("SELECT * FROM lists WHERE id = :listId")
    suspend fun getListById(listId: Int): ListEntity?

    @Query("SELECT * FROM lists")
    suspend fun getAllLists(): List<ListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLists(lists: List<ListEntity>)

    @Query("DELETE FROM lists")
    suspend fun clearAllLists()
}