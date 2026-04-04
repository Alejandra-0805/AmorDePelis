package com.alejandra.amordepelis.features.movies.di

import com.alejandra.amordepelis.features.movies.data.datasources.remote.api.MoviesApi

object MoviesNetworkModule {
    fun provideMoviesApi(): MoviesApi {
        throw NotImplementedError("Provide MoviesApi implementation")
    }
}
