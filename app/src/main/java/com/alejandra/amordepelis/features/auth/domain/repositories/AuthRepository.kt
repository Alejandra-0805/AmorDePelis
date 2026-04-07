package com.alejandra.amordepelis.features.auth.domain.repositories

import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.LoginRequest
import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.RegisterRequest
import com.alejandra.amordepelis.features.auth.domain.entities.LoginResponse
import com.alejandra.amordepelis.features.auth.domain.entities.RegisterResponse

interface AuthRepository {
    suspend fun register(request: RegisterRequest): RegisterResponse

    suspend fun login(request: LoginRequest): LoginResponse
}