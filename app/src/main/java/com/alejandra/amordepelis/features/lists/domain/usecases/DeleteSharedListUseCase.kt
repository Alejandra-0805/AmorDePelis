package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository

class DeleteSharedListUseCase(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(listId: String) = repository.deleteSharedList(listId)
}
