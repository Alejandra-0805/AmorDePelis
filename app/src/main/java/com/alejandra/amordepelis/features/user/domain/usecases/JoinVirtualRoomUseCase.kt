package com.alejandra.amordepelis.features.user.domain.usecases

import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import javax.inject.Inject

class JoinVirtualRoomUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(invitationCode: String) {
        repository.joinVirtualRoom(invitationCode)
    }
}
