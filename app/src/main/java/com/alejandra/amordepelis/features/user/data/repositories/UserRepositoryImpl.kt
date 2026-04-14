package com.alejandra.amordepelis.features.user.data.repositories

import com.alejandra.amordepelis.core.storage.TokenProvider
import com.alejandra.amordepelis.features.user.data.datasources.remote.api.UserApi
import com.alejandra.amordepelis.features.user.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.user.data.datasources.remote.mapper.toPartnerInvitationRequestDto
import com.alejandra.amordepelis.features.user.domain.entities.PartnerInvitation
import com.alejandra.amordepelis.features.user.domain.entities.UserProfile
import com.alejandra.amordepelis.features.user.domain.entities.UserSearchResult
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject
import android.util.Base64

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val tokenProvider: TokenProvider
) : UserRepository {
    override suspend fun getUserProfile(): UserProfile {
        val token = tokenProvider.getToken() ?: throw Exception("No token available")
        val userId = getUserIdFromToken(token) ?: throw Exception("Invalid token or missing ID")
        
        val userProfileDto = userApi.getUserProfile(userId)
        
        var roomName: String? = null
        var ownInviteCode: String? = null
        var hasPartnerFromRoom = false
        var partnerUsernameFromRoom: String? = null
        var partnerEmailFromRoom: String? = null
        
        try {
            val rooms = userApi.getUserRooms()
            if (rooms.isNotEmpty()) {
                // Buscamos primero la sala que esté vinculada (donde haya creator y guest).
                val room = rooms.find { it.creatorId != null && it.guestId != null } ?: rooms.first()
                
                roomName = room.roomName
                ownInviteCode = room.invitationCode
                
                // Si la sala tiene ambos (creador e invitado), detectamos que hay un partner.
                if (room.creatorId != null && room.guestId != null) {
                    hasPartnerFromRoom = true
                    
                    // Tratamos de buscar la info de la pareja consultando el endpoint del otro ID
                    val partnerId = if (room.creatorId == userId) room.guestId else room.creatorId
                    try {
                        val partnerDto = userApi.getUserProfile(partnerId)
                        partnerUsernameFromRoom = partnerDto.username
                        partnerEmailFromRoom = partnerDto.email
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
             // It's possible the user doesn't have a room yet, or network failed just for rooms.
        }

        val profile = userProfileDto.toDomain(roomName, ownInviteCode)

        // Si el DTO original no trajo pareja, pero por salas descubrimos que sí la tiene, la mapeamos forzadamente
        if (profile.partner == null && hasPartnerFromRoom) {
            return profile.copy(
                partner = com.alejandra.amordepelis.features.user.domain.entities.PartnerProfile(
                    id = if (userProfileDto.id == 0) "0" else "999", // Un id de respaldo si es necesario
                    username = partnerUsernameFromRoom ?: "Pareja vinculada",
                    email = partnerEmailFromRoom ?: "..."
                )
            )
        }
        
        return profile
    }

    private fun getUserIdFromToken(token: String): Int? {
        return try {
            val parts = token.split(".")
            if (parts.size == 3) {
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
                val json = JSONObject(payload)
                json.getInt("userId")
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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

    override suspend fun joinVirtualRoom(invitationCode: String) {
        userApi.joinVirtualRoom(
            com.alejandra.amordepelis.features.user.data.datasources.remote.model.JoinRoomRequestDto(invitationCode)
        )
    }
}
