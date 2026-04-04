package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository

class GetSharedListsUseCase(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(): List<SharedList> = repository.getSharedLists()
}
