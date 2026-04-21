package com.alejandra.amordepelis.features.lists.data.datasources.remote

import com.alejandra.amordepelis.features.lists.data.datasources.remote.api.ListsApi
import com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.lists.data.datasources.remote.mapper.toSharedListDetails
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CreateListRequestDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CustomListResponseDto
import com.alejandra.amordepelis.features.lists.domain.entities.SharedList
import com.alejandra.amordepelis.features.lists.domain.entities.SharedListDetails
import javax.inject.Inject

/**
 * DataSource remoto para operaciones de listas desde la API
 * Implementa el patrón de abstracción para separar concerns
 * Principio SOLID: Single Responsibility - solo maneja operaciones remotas
 */
class RemoteListDataSource @Inject constructor(
    private val listsApi: ListsApi
) {

    /**
     * Obtiene todas las listas de una sala desde el servidor
     * @param roomId ID de la sala
     * @return Lista de listas desde la API
     * @throws Exception si hay error en la conexión o procesamiento
     */
    suspend fun getListsByRoom(roomId: Int): List<CustomListResponseDto> {
        return listsApi.getRoomLists(roomId)
    }

    /**
     * Obtiene todas las listas de una sala convertidas a dominio
     * @param roomId ID de la sala
     * @return Lista de listas mapeadas al dominio
     */
    suspend fun getListsByRoomAsDomain(roomId: Int): List<SharedList> {
        return getListsByRoom(roomId).map { it.toDomain() }
    }

    /**
     * Obtiene detalles de una lista específica
     * @param roomId ID de la sala
     * @param listId ID de la lista
     * @return Detalles de la lista con películas
     */
    suspend fun getListDetails(roomId: Int, listId: Int): SharedListDetails {
        return listsApi.getListDetails(roomId, listId).toDomain()
    }

    /**
     * Crea una nueva lista en una sala
     * @param roomId ID de la sala
     * @param name Nombre de la lista
     * @param description Descripción de la lista
     * @param colorHex Color hexadecimal (opcional)
     * @return Lista creada
     */
    suspend fun createList(
        roomId: Int,
        name: String,
        description: String = "",
        colorHex: String = "#3B82F6"
    ): SharedList {
        val request = CreateListRequestDto(
            name = name,
            description = description,
            colorHex = colorHex
        )
        return listsApi.createRoomList(roomId, request).toDomain()
    }

    /**
     * Añade una película a una lista
     * @param roomId ID de la sala
     * @param listId ID de la lista
     * @param movieId ID de la película
     */
    suspend fun addMovieToList(roomId: Int, listId: Int, movieId: Int) {
        listsApi.addMovieToList(roomId, listId, movieId)
    }
}
