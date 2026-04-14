package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import javax.inject.Inject

class GetSharedListDetailsUseCase @Inject constructor(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(listId: String): SharedListDetails = repository.getSharedListDetails(listId)
}
