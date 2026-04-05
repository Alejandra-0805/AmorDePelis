package com.alejandra.amordepelis.features.lists.domain.usecases

import com.alejandra.amordepelis.features.lists.domain.entities.UpdateListParams
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import javax.inject.Inject

class UpdateSharedListUseCase @Inject constructor(
    private val repository: ListsRepository
) {
    suspend operator fun invoke(params: UpdateListParams) = repository.updateSharedList(params)
}
