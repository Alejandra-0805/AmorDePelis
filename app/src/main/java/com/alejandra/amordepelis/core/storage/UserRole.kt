package com.alejandra.amordepelis.core.storage

/**
 * Enum que representa los roles de usuario en la aplicación.
 * Define los permisos y acciones permitidas para cada tipo de usuario.
 */
enum class UserRole(val value: String) {
    /**
     * Usuario ADMIN:
     * - Puede ver el catálogo de películas
     * - Puede agregar películas al catálogo
     * - Puede agregar productos
     * - NO puede agregar películas a listas personales
     * - NO puede marcar películas como vistas
     */
    ADMIN("ADMIN"),

    /**
     * Usuario PAREJA:
     * - Puede ver el catálogo de películas
     * - Puede agregar películas a listas personales
     * - Puede marcar películas como vistas/favoritas
     * - NO puede agregar películas al catálogo
     * - NO puede agregar productos
     */
    PAREJA("PAREJA");

    companion object {
        fun fromString(value: String): UserRole {
            return entries.find { it.value.equals(value, ignoreCase = true) } ?: PAREJA
        }
    }

    // Permisos de ADMIN
    fun canAddMoviesToCatalog(): Boolean = this == ADMIN
    fun canAddProducts(): Boolean = this == ADMIN
    fun canAddNews(): Boolean = this == ADMIN

    // Permisos de PAREJA
    fun canAddMoviesToPersonalLists(): Boolean = this == PAREJA
    fun canMarkMoviesAsWatched(): Boolean = this == PAREJA
    fun canMarkMoviesAsFavorite(): Boolean = this == PAREJA
    fun canCreateLists(): Boolean = this == PAREJA

    // Permisos comunes
    fun canViewMovieCatalog(): Boolean = true
    fun canViewProducts(): Boolean = true
    fun canViewNews(): Boolean = true
}
