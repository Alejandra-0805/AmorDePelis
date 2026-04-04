package com.alejandra.amordepelis.features.user.domain.usecases

import com.alejandra.amordepelis.features.user.domain.entities.PartnerInvitation
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository

class SendPartnerInvitationUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(targetUserId: String) {
        repository.sendPartnerInvitation(
            PartnerInvitation(targetUserId = targetUserId)
        )
    }
}
