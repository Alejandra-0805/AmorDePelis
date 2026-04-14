package com.alejandra.amordepelis.features.user.data.datasources.remote.api

import com.alejandra.amordepelis.features.user.data.datasources.remote.model.PartnerInvitationRequestDto
import com.alejandra.amordepelis.features.user.data.datasources.remote.model.UserProfileDto
import com.alejandra.amordepelis.features.user.data.datasources.remote.model.UserSearchResultDto
import com.alejandra.amordepelis.features.user.data.datasources.remote.model.JoinRoomRequestDto
import com.alejandra.amordepelis.features.user.data.datasources.remote.model.RoomResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") id: Int): UserProfileDto

    @GET("users/search")
    suspend fun searchUsersByUsername(@Query("username") username: String): List<UserSearchResultDto>

    @POST("users/partner/invite")
    suspend fun sendPartnerInvitation(@Body request: PartnerInvitationRequestDto)

    @Multipart
    @PUT("users/{id}")
    suspend fun updateUserProfile(
        @Path("id") id: String,
        @Part("username") username: RequestBody,
        @Part image: MultipartBody.Part?
    )

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String)

    @POST("rooms/join")
    suspend fun joinVirtualRoom(@Body request: JoinRoomRequestDto): RoomResponseDto

    @GET("rooms")
    suspend fun getUserRooms(): List<RoomResponseDto>
}
