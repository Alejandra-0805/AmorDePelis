package com.alejandra.amordepelis.features.lists.domain.repositories

import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails

interface ListsRepository {
    suspend fun getSharedLists(roomId: Int): List<SharedList>
    suspend fun getSharedListDetails(roomId: Int, listId: Int, listName: String): SharedListDetails
    suspend fun createSharedList(params: CreateListParams): SharedList
    suspend fun addMovieToList(roomId: Int, listId: Int, movieId: Int)
}
