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

    /**
     * ID de la sala activa del usuario.
     * Se inicializa desde el cache local (DataStore) al arrancar, por lo que
     * está disponible incluso sin conexión a internet.
     */
    private val _currentRoomId = MutableStateFlow<Int?>(null)
    val currentRoomId: StateFlow<Int?> = _currentRoomId.asStateFlow()

    @Volatile
    private var cachedRole: UserRole = UserRole.PAREJA

    init {
        scope.launch {
            // Cargar rol inicial desde cache
            cachedRole = tokenDataStore.getRole()
            _currentRole.value = cachedRole
            _isLoggedIn.value = tokenDataStore.getToken() != null
            // Cargar roomId inicial desde cache (disponible offline)
            _currentRoomId.value = tokenDataStore.getRoomId()

            // Observar cambios en el rol
            tokenDataStore.roleFlow.collect { role ->
                cachedRole = role
                _currentRole.value = role
            }
        }

        scope.launch {
            // Observar cambios en el roomId persistido
            tokenDataStore.roomIdFlow.collect { roomId ->
                _currentRoomId.value = roomId
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

    /**
     * Guarda la sesión del usuario después del login.
     */
    suspend fun saveSession(token: String, role: String) {
        tokenProvider.forceUpdateToken(token)
        tokenDataStore.saveSession(token, role)
        cachedRole = UserRole.fromString(role)
        _currentRole.value = cachedRole
        _isLoggedIn.value = true
    }

    /**
     * Persiste el roomId localmente y actualiza el StateFlow.
     * Llamar después de obtener el roomId de la API con éxito.
     */
    suspend fun saveRoomId(roomId: Int) {
        tokenDataStore.saveRoomId(roomId)
        _currentRoomId.value = roomId
    }

    /** Retorna el roomId cacheado de forma síncrona. */
    fun getRoomId(): Int? = _currentRoomId.value

    /**
     * Cierra la sesión del usuario
     */
    suspend fun logout() {
        tokenProvider.forceUpdateToken(null)
        tokenDataStore.clearSession()
        cachedRole = UserRole.PAREJA
        _currentRole.value = UserRole.PAREJA
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
