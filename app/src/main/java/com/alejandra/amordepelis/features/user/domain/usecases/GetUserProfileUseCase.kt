package com.alejandra.amordepelis.features.user.domain.usecases

import com.alejandra.amordepelis.features.user.domain.entities.UserProfile
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): UserProfile = repository.getUserProfile()
}
