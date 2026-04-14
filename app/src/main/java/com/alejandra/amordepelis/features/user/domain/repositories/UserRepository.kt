package com.alejandra.amordepelis.features.user.domain.repositories

import com.alejandra.amordepelis.features.user.domain.entities.PartnerInvitation
import com.alejandra.amordepelis.features.user.domain.entities.UserProfile
import com.alejandra.amordepelis.features.user.domain.entities.UserSearchResult

interface UserRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun searchUsersByUsername(username: String): List<UserSearchResult>
    suspend fun sendPartnerInvitation(invitation: PartnerInvitation)
    suspend fun updateUserProfile(id: String, username: String)
    suspend fun deleteUser(id: String)
    suspend fun joinVirtualRoom(invitationCode: String)
}
