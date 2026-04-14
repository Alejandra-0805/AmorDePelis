package com.alejandra.amordepelis.features.auth.data.repositories

import android.util.Log
import com.alejandra.amordepelis.features.auth.data.datasources.remote.api.AuthApi
import com.alejandra.amordepelis.features.auth.data.datasources.remote.mapper.loginToDomain
import com.alejandra.amordepelis.features.auth.data.datasources.remote.mapper.registerToDomain
import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.LoginRequest
import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.RegisterRequest
import com.alejandra.amordepelis.features.auth.domain.entities.LoginResponse
import com.alejandra.amordepelis.features.auth.domain.entities.RegisterResponse
import com.alejandra.amordepelis.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun register(request: RegisterRequest): RegisterResponse {
        val response = api.register(request)
        Log.d("AuthRepositoryImpl", "register request: $request")
        Log.d("AuthRepositoryImpl", "register response: $response")
        return response.registerToDomain()
    }

    override suspend fun login(request: LoginRequest): LoginResponse {
        val response = api.login(request)
        Log.d("AuthRepositoryImpl", "login request: $request")
        Log.d("AuthRepositoryImpl", "login response: $response")
        return response.loginToDomain()
    }

    override suspend fun createRoom(roomName: String) {
        val request = com.alejandra.amordepelis.features.auth.data.datasources.remote.api.CreateRoomRequestDto(roomName)
        api.createRoom(request)
        Log.d("AuthRepositoryImpl", "Room '$roomName' created automatically.")
    }
}