package com.alejandra.amordepelis.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.alejandra.amordepelis.core.database.entities.UserEntity
import com.alejandra.amordepelis.features.user.domain.entities.UserProfile

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :id")
    fun getById(id: Int): UserEntity?
}