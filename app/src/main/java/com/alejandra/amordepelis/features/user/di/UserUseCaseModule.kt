package com.alejandra.amordepelis.features.user.di

import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import com.alejandra.amordepelis.features.user.domain.usecases.GetUserProfileUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.UpdateUserProfileUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.DeleteUserUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.JoinVirtualRoomUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.UserUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserUseCaseModule {

    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(repository: UserRepository): GetUserProfileUseCase {
        return GetUserProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserProfileUseCase(repository: UserRepository): UpdateUserProfileUseCase {
        return UpdateUserProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteUserUseCase(repository: UserRepository): DeleteUserUseCase {
        return DeleteUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideJoinVirtualRoomUseCase(repository: UserRepository): JoinVirtualRoomUseCase {
        return JoinVirtualRoomUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUserUseCases(
        getUserProfileUseCase: GetUserProfileUseCase,
        updateUserProfileUseCase: UpdateUserProfileUseCase,
        deleteUserUseCase: DeleteUserUseCase,
        joinVirtualRoomUseCase: JoinVirtualRoomUseCase
    ): UserUseCases {
        return UserUseCases(
            getUserProfile = getUserProfileUseCase,
            updateUserProfile = updateUserProfileUseCase,
            deleteUser = deleteUserUseCase,
            joinVirtualRoom = joinVirtualRoomUseCase
        )
    }
}
