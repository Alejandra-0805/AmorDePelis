package com.alejandra.amordepelis.features.user.domain.repositories

import com.alejandra.amordepelis.features.user.domain.entities.UserProfile

interface UserRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun updateUserProfile(id: String, username: String)
    suspend fun deleteUser(id: String)
    suspend fun joinVirtualRoom(invitationCode: String)
}
