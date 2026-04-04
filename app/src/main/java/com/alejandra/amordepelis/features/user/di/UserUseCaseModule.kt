package com.alejandra.amordepelis.features.user.di

import com.alejandra.amordepelis.features.user.domain.usecases.GetUserProfileUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.SearchUsersByUsernameUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.SendPartnerInvitationUseCase
import com.alejandra.amordepelis.features.user.domain.usecases.UserUseCases

object UserUseCaseModule {
    fun provideUserUseCases(): UserUseCases {
        val repository = UserRepositoryModule.provideUserRepository()
        return UserUseCases(
            getUserProfile = GetUserProfileUseCase(repository),
            searchUsersByUsername = SearchUsersByUsernameUseCase(repository),
            sendPartnerInvitation = SendPartnerInvitationUseCase(repository)
        )
    }
}
