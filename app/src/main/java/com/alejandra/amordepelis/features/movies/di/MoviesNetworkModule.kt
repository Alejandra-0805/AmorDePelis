package com.alejandra.amordepelis.features.movies.di

import com.alejandra.amordepelis.core.di.NetworkModule
import com.alejandra.amordepelis.features.movies.data.datasources.remote.api.MoviesApi

object MoviesNetworkModule {
    fun provideMoviesApi(): MoviesApi {
        return NetworkModule.provideRetrofit(
            gson = NetworkModule.provideGson(),
            baseUrl = NetworkModule.provideBaseUrl()
        ).create(MoviesApi::class.java)
    }
}
