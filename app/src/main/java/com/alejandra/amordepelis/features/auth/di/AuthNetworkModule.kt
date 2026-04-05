package com.alejandra.amordepelis.features.auth.di

import com.alejandra.amordepelis.core.di.AmorDePelisApi
import com.alejandra.amordepelis.features.auth.data.datasources.remote.api.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {
    @Provides
    @Singleton
    fun provideAuthApi(@AmorDePelisApi retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }
}