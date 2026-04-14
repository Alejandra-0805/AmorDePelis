package com.alejandra.amordepelis.features.user.domain.usecases

import com.alejandra.amordepelis.features.user.domain.entities.UserSearchResult
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import javax.inject.Inject

class SearchUsersByUsernameUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(username: String): List<UserSearchResult> {
        return repository.searchUsersByUsername(username)
    }
}
