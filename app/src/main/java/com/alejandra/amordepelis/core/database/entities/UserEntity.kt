package com.alejandra.amordepelis.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para cachear el perfil del usuario localmente.
 *
 * Offline-First: almacena todos los datos que la UI de perfil necesita
 * para funcionar sin conexión a internet.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val username: String,
    val email: String,
    val passwordMasked: String,
    val roomName: String? = null,
    val ownInviteCode: String? = null,
    // Datos de pareja para mostrar la sección vinculada offline
    val hasPartner: Boolean = false,
    val partnerUsername: String? = null,
    val partnerEmail: String? = null,
    val role: String = ""
)
