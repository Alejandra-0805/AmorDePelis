package com.alejandra.amordepelis.features.lists.data.datasources.remote.api

import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.AnnouncementDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CreateListRequestDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.SharedListDetailsDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.SharedListDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.UpdateListRequestDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CustomListDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ListsApi {
    @GET("lists")
    suspend fun getSharedLists(): List<SharedListDto>

    @GET("lists/{listId}")
    suspend fun getSharedListDetails(@Path("listId") listId: String): SharedListDetailsDto

    @POST("rooms/{roomId}/lists")
    suspend fun createSharedListByRoom(
        @Path("roomId") roomId: String,
        @Body request: CreateListRequestDto
    ): SharedListDto

    @PUT("lists")
    suspend fun updateSharedList(@Body request: UpdateListRequestDto)

    @DELETE("lists/{listId}")
    suspend fun deleteSharedList(@Path("listId") listId: String)

    @GET("lists/announcements")
    suspend fun getAnnouncements(): List<AnnouncementDto>

    @GET("rooms/{roomId}/lists")
    suspend fun getListsByRoom(@Path("roomId") roomId: String): List<SharedListDto>

    @POST("rooms/{roomId}/lists/{listId}/movies/{movieId}")
    suspend fun addMovieToList(
        @Path("roomId") roomId: String,
        @Path("listId") listId: String,
        @Path("movieId") movieId: String
    )
}
