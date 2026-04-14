package com.alejandra.amordepelis.features.user.domain.usecases

import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteUser(id)
    }
}