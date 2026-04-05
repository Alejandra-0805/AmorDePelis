package com.alejandra.amordepelis.features.home.di

import com.alejandra.amordepelis.features.home.data.repositories.HomeRepositoryImpl
import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeRepositoryModule {
    @Binds
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository
}
