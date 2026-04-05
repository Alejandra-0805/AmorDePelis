package com.alejandra.amordepelis.features.lists.di

import com.alejandra.amordepelis.core.di.AmorDePelisApi
import com.alejandra.amordepelis.features.lists.data.datasources.remote.api.ListsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ListsNetworkModule {
    @Provides
    @Singleton
    fun provideListsApi(@AmorDePelisApi retrofit: Retrofit): ListsApi {
        return retrofit.create(ListsApi::class.java)
    }
}
