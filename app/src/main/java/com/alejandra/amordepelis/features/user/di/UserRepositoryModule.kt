package com.alejandra.amordepelis.features.user.di

import com.alejandra.amordepelis.features.user.data.repositories.UserRepositoryImpl
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
