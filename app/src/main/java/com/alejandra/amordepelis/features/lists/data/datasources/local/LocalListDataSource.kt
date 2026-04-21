package com.alejandra.amordepelis.features.lists.data.datasources.local

import com.alejandra.amordepelis.core.database.dao.ListDao
import com.alejandra.amordepelis.core.database.entities.ListEntity
import com.alejandra.amordepelis.features.lists.data.datasources.local.mapper.toDomain
import com.alejandra.amordepelis.features.lists.data.datasources.local.mapper.toDomainList
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import javax.inject.Inject

class LocalListDataSource @Inject constructor(
    private val listDao: ListDao
) {

    suspend fun getListsByRoom(roomId: Int): List<SharedList> {
        return listDao.getListsBySalaId(roomId).toDomainList()
    }


    suspend fun getListById(listId: Int): SharedList? {
        return listDao.getListById(listId)?.toDomain()
    }

    suspend fun getAllLists(): List<SharedList> {
        return listDao.getAllLists().toDomainList()
    }

    suspend fun saveList(list: ListEntity) {
        listDao.insertList(list)
    }

    suspend fun saveLists(lists: List<ListEntity>) {
        listDao.insertLists(lists)
    }

    suspend fun clearAllLists() {
        listDao.clearAllLists()
    }


    suspend fun replaceAllLists(lists: List<ListEntity>) {
        clearAllLists()
        saveLists(lists)
    }
}
