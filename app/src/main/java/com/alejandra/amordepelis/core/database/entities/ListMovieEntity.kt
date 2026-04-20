package com.alejandra.amordepelis.core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "lists_movies",
    primaryKeys = ["listId", "movieId"],
    foreignKeys = [
        ForeignKey(
            entity = ListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ListMovieEntity (
    @ColumnInfo(name = "listId")
    val listId: Int,
    @ColumnInfo(name = "movieId")
    val movieId: Int
)