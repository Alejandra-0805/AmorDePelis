package com.alejandra.amordepelis.features.user.di

import com.alejandra.amordepelis.core.di.AmorDePelisApi
import com.alejandra.amordepelis.features.movies.data.datasources.remote.api.MoviesApi
import com.alejandra.amordepelis.features.user.data.datasources.remote.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserNetworkModule {
    @Provides
    @Singleton
    fun provideUserApi(@AmorDePelisApi retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
}