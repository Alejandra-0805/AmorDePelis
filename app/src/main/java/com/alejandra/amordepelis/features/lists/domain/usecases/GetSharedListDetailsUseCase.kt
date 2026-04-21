package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import javax.inject.Inject

class GetSharedListDetailsUseCase @Inject constructor(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(roomId: Int, listId: Int, listName: String): SharedListDetails {
        return repository.getSharedListDetails(roomId, listId)
    }
}
