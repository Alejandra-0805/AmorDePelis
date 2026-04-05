package com.alejandra.amordepelis.features.movies.di

import com.alejandra.amordepelis.core.di.AmorDePelisApi
import com.alejandra.amordepelis.features.movies.data.datasources.remote.api.MoviesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoviesNetworkModule {
    @Provides
    @Singleton
    fun provideMoviesApi(@AmorDePelisApi retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }
}