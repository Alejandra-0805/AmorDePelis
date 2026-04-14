package com.alejandra.amordepelis.features.auth.domain.usecases

import com.alejandra.amordepelis.features.auth.data.datasources.remote.model.RegisterRequest
import com.alejandra.amordepelis.features.auth.domain.entities.RegisterResponse
import com.alejandra.amordepelis.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = authRepository.register(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}