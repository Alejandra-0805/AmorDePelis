package com.alejandra.amordepelis.core.storage

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SessionManager gestiona la sesión del usuario actual incluyendo
 * el token de autenticación y el rol del usuario.
 * Proporciona acceso reactivo al rol para UI y funciones de verificación de permisos.
 */
@Singleton
class SessionManager @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val tokenProvider: TokenProvider
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _currentRole = MutableStateFlow(UserRole.PAREJA)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentRoomId = MutableStateFlow<String?>(null)
    val currentRoomId: StateFlow<String?> = _currentRoomId.asStateFlow()

    @Volatile
    private var cachedRole: UserRole = UserRole.PAREJA

    @Volatile
    private var cachedRoomId: String? = null

    init {
        scope.launch {
            // Cargar rol inicial
            cachedRole = tokenDataStore.getRole()
            _currentRole.value = cachedRole
            
            cachedRoomId = tokenDataStore.getRoomId()
            _currentRoomId.value = cachedRoomId
            
            _isLoggedIn.value = tokenDataStore.getToken() != null

            // Observar cambios en el rol
            launch {
                tokenDataStore.roleFlow.collect { role ->
                    cachedRole = role
                    _currentRole.value = role
                }
            }
            
            launch {
                tokenDataStore.roomIdFlow.collect { roomId ->
                    cachedRoomId = roomId
                    _currentRoomId.value = roomId
                }
            }
        }

        scope.launch {
            tokenDataStore.tokenFlow.collect { token ->
                tokenProvider.forceUpdateToken(token)
                _isLoggedIn.value = token != null
            }
        }
    }

    /**
     * Obtiene el rol actual de forma síncrona (usa cache)
     */
    fun getRole(): UserRole = cachedRole

    fun getRoomId(): String? = cachedRoomId

    suspend fun saveRoomId(roomId: String) {
        tokenDataStore.saveRoomId(roomId)
        cachedRoomId = roomId
        _currentRoomId.value = roomId
    }

    /**
     * Guarda la sesión del usuario después del login
     */
    suspend fun saveSession(token: String, role: String) {
        tokenProvider.forceUpdateToken(token)
        tokenDataStore.saveSession(token, role)
        cachedRole = UserRole.fromString(role)
        _currentRole.value = cachedRole
        _isLoggedIn.value = true
    }

    /**
     * Cierra la sesión del usuario
     */
    suspend fun logout() {
        tokenProvider.forceUpdateToken(null)
        tokenDataStore.clearSession()
        cachedRole = UserRole.PAREJA
        _currentRole.value = UserRole.PAREJA
        cachedRoomId = null
        _currentRoomId.value = null
        _isLoggedIn.value = false
    }

    // Funciones de permisos para uso directo en la UI
    fun canAddMoviesToCatalog(): Boolean = cachedRole.canAddMoviesToCatalog()
    fun canAddProducts(): Boolean = cachedRole.canAddProducts()
    fun canAddNews(): Boolean = cachedRole.canAddNews()
    fun canAddMoviesToPersonalLists(): Boolean = cachedRole.canAddMoviesToPersonalLists()
    fun canMarkMoviesAsWatched(): Boolean = cachedRole.canMarkMoviesAsWatched()
    fun canMarkMoviesAsFavorite(): Boolean = cachedRole.canMarkMoviesAsFavorite()
    fun canCreateLists(): Boolean = cachedRole.canCreateLists()

    fun isAdmin(): Boolean = cachedRole == UserRole.ADMIN
    fun isPareja(): Boolean = cachedRole == UserRole.PAREJA
}
