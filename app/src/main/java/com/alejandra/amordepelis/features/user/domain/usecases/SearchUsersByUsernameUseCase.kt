package com.alejandra.amordepelis.features.user.domain.usecases

import com.alejandra.amordepelis.features.user.domain.entities.UserSearchResult
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository

class SearchUsersByUsernameUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String): List<UserSearchResult> {
        return repository.searchUsersByUsername(username)
    }
}
