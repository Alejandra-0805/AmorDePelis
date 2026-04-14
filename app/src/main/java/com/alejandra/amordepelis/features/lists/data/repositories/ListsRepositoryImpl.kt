package com.alejandra.amordepelis.features.lists.data.repositories

import com.alejandra.amordepelis.features.lists.data.datasources.remote.api.ListsApi
import com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper.toAnnouncementDomainList
import com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper.toDto
import com.alejandra.amordepelis.features.lists.domain.entities.Announcement
import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails
import com.alejandra.amordepelis.features.lists.domain.entities.UpdateListParams
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import javax.inject.Inject

class ListsRepositoryImpl @Inject constructor(
    private val api: ListsApi
) : ListsRepository {
    override suspend fun getSharedLists(roomId: String): List<SharedList> {
        return api.getListsByRoom(roomId).map { it.toDomain() }
    }

    override suspend fun getSharedListDetails(listId: String): SharedListDetails {
        return api.getSharedListDetails(listId).toDomain()
    }

    override suspend fun createSharedList(roomId: String, params: CreateListParams): SharedList {
        return api.createSharedListByRoom(roomId, params.toDto()).toDomain()
    }

    override suspend fun updateSharedList(params: UpdateListParams) {
        api.updateSharedList(params.toDto())
    }

    override suspend fun deleteSharedList(listId: String) {
        api.deleteSharedList(listId)
    }

    override suspend fun getAnnouncements(): List<Announcement> {
        return api.getAnnouncements().toAnnouncementDomainList()
    }
}
