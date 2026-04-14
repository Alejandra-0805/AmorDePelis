package com.alejandra.amordepelis.features.auth.domain.usecases

import com.alejandra.amordepelis.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class CreateAutoRoomUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(roomName: String): Result<Unit> {
        return try {
            authRepository.createRoom(roomName)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
