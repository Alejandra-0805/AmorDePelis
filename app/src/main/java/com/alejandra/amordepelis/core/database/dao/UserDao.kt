package com.alejandra.amordepelis.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.alejandra.amordepelis.core.database.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getById(id: Int): UserEntity?
}