package com.alejandra.amordepelis.features.lists.data.datasources.remote.api

import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CreateListRequestDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.SharedListDetailsDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.SharedListDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.UpdateListRequestDto

interface ListsApi {
    suspend fun getSharedLists(): List<SharedListDto>
    suspend fun getSharedListDetails(listId: String): SharedListDetailsDto
    suspend fun createSharedList(request: CreateListRequestDto): SharedListDto
    suspend fun updateSharedList(request: UpdateListRequestDto)
    suspend fun deleteSharedList(listId: String)
}
