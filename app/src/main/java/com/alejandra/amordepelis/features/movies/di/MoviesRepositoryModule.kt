package com.alejandra.amordepelis.features.movies.di

import com.alejandra.amordepelis.features.movies.data.repositories.MoviesRepositoryImpl
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MoviesRepositoryModule {
    @Binds
    abstract fun bindMoviesRepository(
        moviesRepositoryImpl: MoviesRepositoryImpl
    ): MoviesRepository
}
