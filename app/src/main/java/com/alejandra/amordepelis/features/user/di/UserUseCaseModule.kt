package com.alejandra.amordepelis.features.user.di

import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import com.alejandra.amordepelis.features.user.domain.usecases.GetUserProfileUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.SearchUsersByUsernameUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.SendPartnerInvitationUseCase
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
    fun provideUserUseCases(
        getUserProfileUseCase: GetUserProfileUseCase,
        searchUsersByUsernameUseCase: SearchUsersByUsernameUseCase,
        sendPartnerInvitationUseCase: SendPartnerInvitationUseCase
    ): UserUseCases {
        return UserUseCases(
            getUserProfile = getUserProfileUseCase,
            searchUsersByUsername = searchUsersByUsernameUseCase,
            sendPartnerInvitation = sendPartnerInvitationUseCase
        )
    }
}
