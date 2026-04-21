package com.alejandra.amordepelis.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alejandra.amordepelis.core.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.contracts.contract

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java,"app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideMovieDao(database: AppDatabase) = database.movieDao()
    @Provides
    fun provideListDao(database: AppDatabase) = database.listDao()
    @Provides
    fun provideListMovieDao(database: AppDatabase) = database.listMovieDao()
    @Provides
    fun provideProductDao(database: AppDatabase) = database.productDao()
    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()
}