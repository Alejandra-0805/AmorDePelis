package com.alejandra.amordepelis.features.lists.data.datasources.remote.api

import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.AnnouncementDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CreateListRequestDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.SharedListDetailsDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.SharedListDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.UpdateListRequestDto
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

    @POST("lists")
    suspend fun createSharedList(@Body request: CreateListRequestDto): SharedListDto

    @PUT("lists")
    suspend fun updateSharedList(@Body request: UpdateListRequestDto)

    @DELETE("lists/{listId}")
    suspend fun deleteSharedList(@Path("listId") listId: String)

    @GET("lists/announcements")
    suspend fun getAnnouncements(): List<AnnouncementDto>
}
