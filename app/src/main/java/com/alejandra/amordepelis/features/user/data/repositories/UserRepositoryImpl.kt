package com.alejandra.amordepelis.features.user.data.repositories

import com.alejandra.amordepelis.features.user.data.datasources.remote.api.UserApi
import com.alejandra.amordepelis.features.user.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.user.data.datasources.remote.mapper.toPartnerInvitationRequestDto
import com.alejandra.amordepelis.features.user.domain.entities.PartnerInvitation
import com.alejandra.amordepelis.features.user.domain.entities.UserProfile
import com.alejandra.amordepelis.features.user.domain.entities.UserSearchResult
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
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

    override suspend fun updateUserProfile(id: String, username: String) {
        val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
        userApi.updateUserProfile(id, usernameBody, null)
    }

    override suspend fun deleteUser(id: String) {
        userApi.deleteUser(id)
    }
}
