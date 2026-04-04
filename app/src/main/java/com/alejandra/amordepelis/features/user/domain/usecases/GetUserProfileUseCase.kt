package com.alejandra.amordepelis.features.user.domain.usecases

import com.alejandra.amordepelis.features.user.domain.entities.UserProfile
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository

class GetUserProfileUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): UserProfile = repository.getUserProfile()
}
