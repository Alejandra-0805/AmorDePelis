package com.alejandra.amordepelis.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alejandra.amordepelis.core.database.dao.ListDao
import com.alejandra.amordepelis.core.database.dao.ListMovieDao
import com.alejandra.amordepelis.core.database.dao.MovieDao
import com.alejandra.amordepelis.core.database.dao.ProductDao
import com.alejandra.amordepelis.core.database.dao.UserDao
import com.alejandra.amordepelis.core.database.entities.UserEntity
import com.alejandra.amordepelis.core.database.entities.ProductEntity
import com.alejandra.amordepelis.core.database.entities.MovieEntity
import com.alejandra.amordepelis.core.database.entities.ListEntity
import com.alejandra.amordepelis.core.database.entities.ListMovieEntity

@Database(entities = [
                        UserEntity::class,
                        ProductEntity::class,
                        ListEntity::class,
                        ListMovieEntity::class,
                        MovieEntity::class
                     ], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun listDao(): ListDao
    abstract fun movieDao(): MovieDao
    abstract fun listMovieDao(): ListMovieDao
}