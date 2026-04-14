package com.alejandra.amordepelis.features.user.domain.usecases

import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: String, username: String) {
        repository.updateUserProfile(id, username)
    }
}