package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import javax.inject.Inject

class GetSharedListsUseCase @Inject constructor(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(roomId: String): List<SharedList> = repository.getSharedLists(roomId)
}
