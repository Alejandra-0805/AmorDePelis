package com.alejandra.amordepelis.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.alejandra.amordepelis.core.storage.UserRole

sealed class BottomNavItem(
    val route: Any,
    val icon: ImageVector,
    val label: String,
    val visibleForRoles: Set<UserRole> = UserRole.entries.toSet()
) {
    data object HomeItem : BottomNavItem(
        route = Home,
        icon = Icons.Default.Home,
        label = "Inicio"
    )

    data object MoviesItem : BottomNavItem(
        route = Movies,
        icon = Icons.Default.Movie,
        label = "Películas"
    )

    // Lists solo visible para PAREJA (ADMIN no puede crear/ver listas personales)
    data object ListsItem : BottomNavItem(
        route = Lists,
        icon = Icons.Default.List,
        label = "Listas",
        visibleForRoles = setOf(UserRole.PAREJA)
    )

    data object UserItem : BottomNavItem(
        route = User,
        icon = Icons.Default.Person,
        label = "Perfil"
    )

    fun isVisibleFor(role: UserRole): Boolean = role in visibleForRoles

    companion object {
        val allItems: List<BottomNavItem> = listOf(
            HomeItem,
            MoviesItem,
            ListsItem,
            UserItem
        )

        fun getItemsForRole(role: UserRole): List<BottomNavItem> {
            return allItems.filter { it.isVisibleFor(role) }
        }

        fun isMainRoute(route: Any?): Boolean {
            return route != null && allItems.any { it.route::class == route::class }
        }

        // Para compatibilidad con código existente
        val items: List<BottomNavItem> = allItems
    }
}
