package com.alejandra.amordepelis.features.lists.data.repositories

import android.util.Log
import com.alejandra.amordepelis.core.network.connectivity.ConnectivityManager
import com.alejandra.amordepelis.features.lists.data.datasources.local.LocalListDataSource
import com.alejandra.amordepelis.features.lists.data.datasources.local.mapper.toEntity
import com.alejandra.amordepelis.features.lists.data.datasources.local.mapper.toEntityList
import com.alejandra.amordepelis.features.lists.data.datasources.remote.RemoteListDataSource
import com.alejandra.amordepelis.features.lists.domain.entities.CreateListParams
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListsRepositoryImpl @Inject constructor(
    private val localListDataSource: LocalListDataSource,
    private val remoteListDataSource: RemoteListDataSource,
    private val connectivityManager: ConnectivityManager
) : ListsRepository {

    companion object {
        private const val TAG = "ListsRepositoryImpl"
    }

    override suspend fun getSharedLists(roomId: Int): List<SharedList> = withContext(Dispatchers.IO) {
        try {
            val localLists = localListDataSource.getListsByRoom(roomId)
            
            if (connectivityManager.isNetworkAvailable()) {
                launchBackgroundSync(roomId)
            } else {
                Log.d(TAG, "Sin conectividad: usando datos locales cacheados")
            }
            
            localLists
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener listas locales", e)
            try {
                remoteListDataSource.getListsByRoomAsDomain(roomId)
            } catch (apiException: Exception) {
                Log.e(TAG, "Error al obtener listas del servidor", apiException)
                emptyList()
            }
        }
    }

    override suspend fun getSharedListDetails(roomId: Int, listId: Int): SharedListDetails = withContext(Dispatchers.IO) {
        try {
            if (connectivityManager.isNetworkAvailable()) {
                remoteListDataSource.getListDetails(roomId, listId)
            } else {
                throw Exception("No hay datos locales para detalles de lista sin conectividad")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener detalles de lista", e)
            throw e
        }
    }

    override suspend fun createSharedList(params: CreateListParams): SharedList = withContext(Dispatchers.IO) {
        try {
            val list = remoteListDataSource.createList(
                roomId = params.roomId,
                name = params.name,
                description = params.description ?: "",
                colorHex = params.colorHex ?: "#3B82F6"
            )
            
            // Guardar en cache local
            localListDataSource.saveList(list.let {
                com.alejandra.amordepelis.core.database.entities.ListEntity(
                    id = it.id.toInt(),
                    name = it.name,
                    description = params.description ?: "",
                    sala_id = params.roomId
                )
            })
            
            Log.d(TAG, "Lista creada y cacheada exitosamente")
            list
        } catch (e: Exception) {
            Log.e(TAG, "Error al crear lista", e)
            throw e
        }
    }

    override suspend fun addMovieToList(roomId: Int, listId: Int, movieId: Int) {
        // Operación que requiere conectividad
        try {
            remoteListDataSource.addMovieToList(roomId, listId, movieId)
            Log.d(TAG, "Película añadida a la lista exitosamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al añadir película a lista", e)
            throw e
        }
    }

    private suspend fun syncListsForRoom(roomId: Int) {
        withContext(Dispatchers.IO) {
            try {
                val remoteLists = remoteListDataSource.getListsByRoom(roomId)
                val listEntities = remoteLists.toEntityList(roomId)
                
                localListDataSource.replaceAllLists(listEntities)
                
                Log.d(TAG, "Listas sincronizadas exitosamente para sala $roomId. Total: ${listEntities.size}")
            } catch (e: Exception) {
                Log.e(TAG, "Error sincronizando listas", e)
                throw e
            }
        }
    }

    private suspend fun launchBackgroundSync(roomId: Int) {
        try {
            syncListsForRoom(roomId)
            Log.d(TAG, "Sincronización en background de listas completada exitosamente")
        } catch (e: Exception) {
            Log.w(TAG, "Sincronización en background de listas falló - usando datos locales", e)
        }
    }
}
