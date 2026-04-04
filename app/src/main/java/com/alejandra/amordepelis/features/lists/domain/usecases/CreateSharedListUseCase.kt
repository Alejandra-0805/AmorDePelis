package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository

class CreateSharedListUseCase(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(params: CreateListParams): SharedList = repository.createSharedList(params)
}
