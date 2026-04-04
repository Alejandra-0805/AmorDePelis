package com.alejandra.amordepelis.features.user.data.repositories

import com.alejandra.amordepelis.features.user.data.datasources.remote.api.UserApi
import com.alejandra.amordepelis.features.user.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.user.data.datasources.remote.mapper.toPartnerInvitationRequestDto
import com.alejandra.amordepelis.features.user.domain.entities.PartnerInvitation
import com.alejandra.amordepelis.features.user.domain.entities.UserProfile
import com.alejandra.amordepelis.features.user.domain.entities.UserSearchResult
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getUserProfile(): UserProfile {
        return userApi.getUserProfile().toDomain()
    }

    override suspend fun searchUsersByUsername(username: String): List<UserSearchResult> {
        return userApi.searchUsersByUsername(username).map { it.toDomain() }
    }

    override suspend fun sendPartnerInvitation(invitation: PartnerInvitation) {
        userApi.sendPartnerInvitation(invitation.targetUserId.toPartnerInvitationRequestDto())
    }
}
