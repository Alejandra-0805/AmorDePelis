package com.alejandra.amordepelis.core.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ProductEntity {
    @Query("SELECT * FROM products")
    fun getAllProducts(): List<ProductEntity>
}