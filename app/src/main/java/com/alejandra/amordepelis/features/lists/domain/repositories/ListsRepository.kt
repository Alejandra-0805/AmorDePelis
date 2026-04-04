package com.alejandra.amordepelis.features.lists.domain.repositories

import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails
import com.alejandra.amordepelis.features.lists.domain.entities.UpdateListParams

interface ListsRepository {
    suspend fun getSharedLists(): List<SharedList>
    suspend fun getSharedListDetails(listId: String): SharedListDetails
    suspend fun createSharedList(params: CreateListParams): SharedList
    suspend fun updateSharedList(params: UpdateListParams)
    suspend fun deleteSharedList(listId: String)
}
