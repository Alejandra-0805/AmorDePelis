package com.alejandra.amordepelis.features.user.data.repositories

import android.util.Base64
import android.util.Log
import com.alejandra.amordepelis.core.network.connectivity.ConnectivityManager
import com.alejandra.amordepelis.core.storage.SessionManager
import com.alejandra.amordepelis.core.storage.TokenProvider
import com.alejandra.amordepelis.features.user.data.datasources.local.LocalUserDataSource
import com.alejandra.amordepelis.features.user.data.datasources.remote.api.UserApi
import com.alejandra.amordepelis.features.user.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.user.domain.entities.PartnerProfile
import com.alejandra.amordepelis.features.user.domain.entities.UserProfile
import com.alejandra.amordepelis.features.user.domain.repositories.UserRepository
import com.alejandra.amordepelis.features.user.data.datasources.remote.model.JoinRoomRequestDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val tokenProvider: TokenProvider,
    private val sessionManager: SessionManager,
    private val localUserDataSource: LocalUserDataSource,
    private val connectivityManager: ConnectivityManager
) : UserRepository {

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    override suspend fun getUserProfile(): UserProfile {
        val token = tokenProvider.getToken() ?: throw Exception("No hay sesión activa")
        val userId = getUserIdFromToken(token)

        val cachedProfile = if (userId != null) {
            localUserDataSource.getProfileById(userId)
        } else {
            localUserDataSource.getFirstCachedProfile()
        }

        if (cachedProfile != null) {
            Log.d(TAG, "Perfil obtenido desde cache local (userId=$userId)")
            if (connectivityManager.isNetworkAvailable()) {
                refreshProfileInBackground(userId)
            }
            return cachedProfile
        }

        if (!connectivityManager.isNetworkAvailable()) {
            throw Exception("Sin conexión y sin datos guardados. Conéctate para cargar tu perfil.")
        }

        return fetchAndCacheProfile(userId)
    }

    private suspend fun fetchAndCacheProfile(userId: Int?): UserProfile {
        val resolvedUserId = userId
            ?: throw Exception("No se pudo determinar el ID de usuario desde el token.")

        val userProfileDto = userApi.getUserProfile(resolvedUserId)

        var roomName: String? = null
        var ownInviteCode: String? = null
        var hasPartnerFromRoom = false
        var partnerUsernameFromRoom: String? = null
        var partnerEmailFromRoom: String? = null

        try {
            val rooms = userApi.getUserRooms()
            if (rooms.isNotEmpty()) {
                val room = rooms.find { it.creatorId != null && it.guestId != null }
                    ?: rooms.first()

                roomName = room.roomName
                ownInviteCode = room.invitationCode

                sessionManager.saveRoomId(room.id)
                Log.d(TAG, "RoomId persistido: ${room.id}")

                if (room.creatorId != null && room.guestId != null) {
                    hasPartnerFromRoom = true
                    val partnerId = if (room.creatorId == resolvedUserId) room.guestId else room.creatorId
                    try {
                        val partnerDto = userApi.getUserProfile(partnerId)
                        partnerUsernameFromRoom = partnerDto.username
                        partnerEmailFromRoom = partnerDto.email
                    } catch (e: Exception) {
                        Log.w(TAG, "No se pudo obtener perfil de pareja: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "No se pudieron obtener las salas: ${e.message}")
        }

        var profile = userProfileDto.toDomain(roomName, ownInviteCode)

        if (profile.partner == null && hasPartnerFromRoom) {
            val displayName = partnerUsernameFromRoom?.takeIf { it.isNotBlank() }
                ?: partnerEmailFromRoom?.takeIf { it.isNotBlank() }
                ?: "Pareja vinculada"

            profile = profile.copy(
                partner = PartnerProfile(
                    id = "remote",
                    username = displayName,
                    email = partnerEmailFromRoom ?: ""
                )
            )
        }

        localUserDataSource.saveProfile(profile)
        Log.d(TAG, "Perfil guardado en cache local: ${profile.username}")

        return profile
    }

    private suspend fun refreshProfileInBackground(userId: Int?) {
        try {
            val fresh = fetchAndCacheProfile(userId)
            Log.d(TAG, "Perfil actualizado en background: ${fresh.username}")
        } catch (e: Exception) {
            Log.w(TAG, "Refresh en background falló — usando cache: ${e.message}")
        }
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
            Log.w(TAG, "No se pudo parsear userId del token: ${e.message}")
            null
        }
    }

    override suspend fun updateUserProfile(id: String, username: String) {
        val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
        userApi.updateUserProfile(id, usernameBody, null)
        localUserDataSource.clearProfile()
    }

    override suspend fun deleteUser(id: String) {
        userApi.deleteUser(id)
        localUserDataSource.clearProfile()
    }

    override suspend fun joinVirtualRoom(invitationCode: String) {
        userApi.joinVirtualRoom(JoinRoomRequestDto(invitationCode))
        localUserDataSource.clearProfile()
    }
}
