package com.alejandra.amordepelis.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class TokenDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val ROLE_KEY = stringPreferencesKey("user_role")
        private val ROOM_ID_KEY = stringPreferencesKey("room_id")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    val roleFlow: Flow<UserRole> = context.dataStore.data.map { preferences ->
        val roleString = preferences[ROLE_KEY] ?: UserRole.PAREJA.value
        UserRole.fromString(roleString)
    }

    val roomIdFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ROOM_ID_KEY]
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { preferences ->
            preferences[ROLE_KEY] = role
        }
    }

    suspend fun saveRoomId(roomId: String) {
        context.dataStore.edit { preferences ->
            preferences[ROOM_ID_KEY] = roomId
        }
    }

    suspend fun saveSession(token: String, role: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[ROLE_KEY] = role
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.first()[TOKEN_KEY]
    }

    suspend fun getRole(): UserRole {
        val roleString = context.dataStore.data.first()[ROLE_KEY] ?: UserRole.PAREJA.value
        return UserRole.fromString(roleString)
    }

    suspend fun getRoomId(): String? {
        return context.dataStore.data.first()[ROOM_ID_KEY]
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(ROLE_KEY)
            preferences.remove(ROOM_ID_KEY)
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}
