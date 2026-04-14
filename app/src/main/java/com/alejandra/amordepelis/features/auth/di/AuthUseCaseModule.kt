package com.alejandra.amordepelis.features.auth.di

import com.alejandra.amordepelis.core.storage.TokenDataStore
import com.alejandra.amordepelis.features.auth.domain.repositories.AuthRepository
import com.alejandra.amordepelis.features.auth.domain.usecases.AuthUseCases
import com.alejandra.amordepelis.features.auth.domain.usecases.LoginUseCase
import com.alejandra.amordepelis.features.auth.domain.usecases.RegisterUseCase
import com.alejandra.amordepelis.features.auth.domain.usecases.SaveTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveTokenUseCase(tokenDataStore: TokenDataStore): SaveTokenUseCase {
        return SaveTokenUseCase(tokenDataStore)
    }

    @Provides
    @Singleton
    fun provideCreateAutoRoomUseCase(repository: AuthRepository): com.alejandra.amordepelis.features.auth.domain.usecases.CreateAutoRoomUseCase {
        return com.alejandra.amordepelis.features.auth.domain.usecases.CreateAutoRoomUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAuthUseCases(
        loginUseCase: LoginUseCase,
        registerUseCase: RegisterUseCase,
        saveTokenUseCase: SaveTokenUseCase,
        createRoomUseCase: com.alejandra.amordepelis.features.auth.domain.usecases.CreateAutoRoomUseCase
    ): AuthUseCases {
        return AuthUseCases(
            login = loginUseCase,
            register = registerUseCase,
            saveToken = saveTokenUseCase,
            createRoom = createRoomUseCase
        )
    }
}