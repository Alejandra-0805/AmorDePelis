package com.alejandra.amordepelis.features.auth.domain.usecases

import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.LoginRequest
import com.alejandra.amordepelis.features.auth.domain.entities.LoginResponse
import com.alejandra.amordepelis.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: LoginRequest): Result<LoginResponse> {
        return try {
            val response = authRepository.login(request)
            if (response.role.isNotEmpty()) {
                Result.success(response)
            } else {
                Result.failure(Exception("No se pudo iniciar sesión"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}