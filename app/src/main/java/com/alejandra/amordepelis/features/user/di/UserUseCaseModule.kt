package com.alejandra.amordepelis.features.user.di

import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import com.alejandra.amordepelis.features.user.domain.usecases.GetUserProfileUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.SearchUsersByUsernameUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.SendPartnerInvitationUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.UpdateUserProfileUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.DeleteUserUseCase
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
    fun provideSearchUsersByUsernameUseCase(repository: UserRepository): SearchUsersByUsernameUseCase {
        return SearchUsersByUsernameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSendPartnerInvitationUseCase(repository: UserRepository): SendPartnerInvitationUseCase {
        return SendPartnerInvitationUseCase(repository)
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
    fun provideJoinVirtualRoomUseCase(repository: UserRepository): com.alejandra.amordepelis.features.user.domain.usecases.JoinVirtualRoomUseCase {
        return com.alejandra.amordepelis.features.user.domain.usecases.JoinVirtualRoomUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUserUseCases(
        getUserProfileUseCase: GetUserProfileUseCase,
        searchUsersByUsernameUseCase: SearchUsersByUsernameUseCase,
        sendPartnerInvitationUseCase: SendPartnerInvitationUseCase,
        updateUserProfileUseCase: UpdateUserProfileUseCase,
        deleteUserUseCase: DeleteUserUseCase,
        joinVirtualRoomUseCase: com.alejandra.amordepelis.features.user.domain.usecases.JoinVirtualRoomUseCase
    ): UserUseCases {
        return UserUseCases(
            getUserProfile = getUserProfileUseCase,
            searchUsersByUsername = searchUsersByUsernameUseCase,
            sendPartnerInvitation = sendPartnerInvitationUseCase,
            updateUserProfile = updateUserProfileUseCase,
            deleteUser = deleteUserUseCase,
            joinVirtualRoom = joinVirtualRoomUseCase
        )
    }
}
