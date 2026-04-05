package com.alejandra.amordepelis.features.user.domain.usecases

import com.alejandra.amordepelis.features.user.domain.entities.PartnerInvitation
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import javax.inject.Inject

class SendPartnerInvitationUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(targetUserId: String) {
        repository.sendPartnerInvitation(
            PartnerInvitation(targetUserId = targetUserId)
        )
    }
}
