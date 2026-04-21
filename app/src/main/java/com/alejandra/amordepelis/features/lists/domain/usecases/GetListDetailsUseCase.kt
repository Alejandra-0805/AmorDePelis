package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import javax.inject.Inject

class GetListDetailsUseCase @Inject constructor(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(roomId: Int, listId: Int): SharedListDetails {
        return repository.getSharedListDetails(roomId, listId)
    }
}