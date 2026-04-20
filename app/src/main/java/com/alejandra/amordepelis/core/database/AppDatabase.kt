package com.alejandra.amordepelis.core.database

import androidx.room.Database

@Database(entities = [User::class], version = 1)
abstract class AppDatabase {
}