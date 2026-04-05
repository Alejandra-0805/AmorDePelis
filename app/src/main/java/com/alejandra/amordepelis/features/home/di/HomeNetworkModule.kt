package com.alejandra.amordepelis.features.home.di

import com.alejandra.amordepelis.core.di.AmorDePelisApi
import com.alejandra.amordepelis.features.home.data.datasources.remote.api.HomeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeNetworkModule {
    @Provides
    @Singleton
    fun provideHomeApi(@AmorDePelisApi retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }
}