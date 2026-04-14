package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import javax.inject.Inject

class CreateSharedListUseCase @Inject constructor(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(roomId: String, params: CreateListParams): SharedList = repository.createSharedList(roomId, params)
}
