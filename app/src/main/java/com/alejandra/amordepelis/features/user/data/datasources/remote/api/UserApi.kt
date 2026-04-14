package com.alejandra.amordepelis.features.user.data.datasources.remote.api

import com.alejandra.amordepelis.features.user.data.datasources.remote.model.PartnerInvitationRequestDto
import com.alejandra.amordepelis.features.user.data.datasources.remote.model.UserProfileDto
import com.alejandra.amordepelis.features.user.data.datasources.remote.model.UserSearchResultDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @GET("users/profile")
    suspend fun getUserProfile(): UserProfileDto

    @GET("users/search")
    suspend fun searchUsersByUsername(@Query("username") username: String): List<UserSearchResultDto>

    @POST("users/partner/invite")
    suspend fun sendPartnerInvitation(@Body request: PartnerInvitationRequestDto)
}
