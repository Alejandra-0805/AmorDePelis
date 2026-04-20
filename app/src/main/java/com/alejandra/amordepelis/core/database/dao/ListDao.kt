package com.alejandra.amordepelis.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.alejandra.amordepelis.core.database.entities.ListEntity

@Dao
interface ListDao {
    @Query("SELECT * FROM lists WHERE sala_id = :salaId")
    fun getListsBySalaId(salaId: Int): List<ListEntity>

    @Query("SELECT * FROM lists WHERE id = :listId")
    fun getListById(listId: Int): ListEntity?
}