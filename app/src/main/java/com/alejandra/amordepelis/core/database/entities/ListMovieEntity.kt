package com.alejandra.amordepelis.core.database.entities

import androidx.room.Entity

@Entity(tableName = "lists_movies")
data class ListMovieEntity (
    val ListId: Int,
    val MovieId: Int,
)