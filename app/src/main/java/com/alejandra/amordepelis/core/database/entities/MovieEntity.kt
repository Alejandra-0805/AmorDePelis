package com.alejandra.amordepelis.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val sinopsis: String,
    val duracion: Int,
    val imageUrl: String
)
