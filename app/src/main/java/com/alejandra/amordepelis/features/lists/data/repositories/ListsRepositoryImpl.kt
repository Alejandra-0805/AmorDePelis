package com.alejandra.amordepelis.features.lists.data.repositories

import com.alejandra.amordepelis.features.lists.data.datasources.remote.api.ListsApi
import com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper.toDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper.toSharedListDetails
import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import javax.inject.Inject

class ListsRepositoryImpl @Inject constructor(
    private val listsApi: ListsApi
) : ListsRepository {

    override suspend fun getSharedLists(roomId: Int): List<SharedList> {
        return listsApi.getRoomLists(roomId).map { it.toDomain() }
    }

    override suspend fun getSharedListDetails(roomId: Int, listId: Int): SharedListDetails {
        return listsApi.getListDetails(roomId, listId).toDomain()
    }

    override suspend fun createSharedList(params: CreateListParams): SharedList {
        return listsApi.createRoomList(params.roomId, params.toDto()).toDomain()
    }

    override suspend fun addMovieToList(roomId: Int, listId: Int, movieId: Int) {
        listsApi.addMovieToList(roomId, listId, movieId)
    }
}
