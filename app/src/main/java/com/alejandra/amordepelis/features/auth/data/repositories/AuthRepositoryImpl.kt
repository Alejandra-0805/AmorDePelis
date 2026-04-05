package com.alejandra.amordepelis.features.auth.data.repositories

import com.alejandra.amordepelis.features.auth.data.datasources.remote.api.AuthApi
import com.alejandra.amordepelis.features.auth.data.datasources.remote.mapper.loginToDomain
import com.alejandra.amordepelis.features.auth.data.datasources.remote.mapper.registerToDomain
import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.LoginRequest
import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.User
import com.alejandra.amordepelis.features.auth.domain.entities.LoginResponse
import com.alejandra.amordepelis.features.auth.domain.entities.RegisterResponse
import com.alejandra.amordepelis.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun register(user: User): RegisterResponse {
        val response = api.register(user)
        return response.registerToDomain()
    }

    override suspend fun login(request: LoginRequest): LoginResponse {
        val response = api.login(request)
        return response.loginToDomain()
    }
}