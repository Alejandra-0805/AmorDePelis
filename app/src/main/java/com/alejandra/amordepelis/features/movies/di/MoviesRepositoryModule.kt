package com.alejandra.amordepelis.features.movies.di

import com.alejandra.amordepelis.features.movies.data.repositories.MoviesRepositoryImpl
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository

class MoviesRepositoryModule {
    fun provideMoviesRepository(): MoviesRepository {
        return MoviesRepositoryImpl(
            moviesApi = MoviesNetworkModule.provideMoviesApi()
        )
    }
}
