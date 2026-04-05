package com.alejandra.amordepelis.features.auth.data.datasources.remote.api

import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.LoginRequest
import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.LoginResponseDto
import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.RegisterResponseDto
import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("users")
    suspend fun register(
        @Body request: User
    ): RegisterResponseDto

    @POST("users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponseDto
}