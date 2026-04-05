package com.alejandra.amordepelis.features.auth.di

import com.alejandra.amordepelis.features.auth.data.repositories.AuthRepositoryImpl
import com.alejandra.amordepelis.features.auth.domain.repositories.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
