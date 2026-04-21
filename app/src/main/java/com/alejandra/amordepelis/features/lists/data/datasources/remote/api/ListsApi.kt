package com.alejandra.amordepelis.features.lists.data.datasources.remote.api

import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CreateListRequestDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.CustomListResponseDto
import com.alejandra.amordepelis.features.lists.data.datasources.remote.model.SharedListDetailsDto
import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ListsApi {
    @GET("rooms/{roomId}/lists")
    suspend fun getRoomLists(@Path("roomId") roomId: Int): List<CustomListResponseDto>

    @GET("rooms/{roomId}/lists/{listId}")
    suspend fun getListDetails(
        @Path("roomId") roomId: Int,
        @Path("listId") listId: Int
    ): SharedListDetailsDto

    @POST("rooms/{roomId}/lists")
    suspend fun createRoomList(
        @Path("roomId") roomId: Int,
        @Body request: CreateListRequestDto
    ): CustomListResponseDto

    @GET("rooms/{roomId}/lists/{listId}/movies")
    suspend fun getListMovies(
        @Path("roomId") roomId: Int,
        @Path("listId") listId: Int
    ): List<MovieDto>

    @POST("rooms/{roomId}/lists/{listId}/movies/{movieId}")
    suspend fun addMovieToList(
        @Path("roomId") roomId: Int,
        @Path("listId") listId: Int,
        @Path("movieId") movieId: Int
    )
}
