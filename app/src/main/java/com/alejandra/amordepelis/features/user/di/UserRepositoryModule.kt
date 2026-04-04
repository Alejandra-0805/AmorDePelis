package com.alejandra.amordepelis.features.user.di

import com.alejandra.amordepelis.features.user.data.repositories.UserRepositoryImpl
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository

class UserRepositoryModule {
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl(
            userApi = UserNetworkModule.provideUserApi()
        )
    }
}
