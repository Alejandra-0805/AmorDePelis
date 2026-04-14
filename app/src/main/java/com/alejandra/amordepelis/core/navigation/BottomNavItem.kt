package com.alejandra.amordepelis.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: Any,
    val icon: ImageVector,
    val label: String
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

    data object ListsItem : BottomNavItem(
        route = Lists,
        icon = Icons.Default.List,
        label = "Listas"
    )

    data object UserItem : BottomNavItem(
        route = User,
        icon = Icons.Default.Person,
        label = "Perfil"
    )

    companion object {
        val items: List<BottomNavItem> = listOf(
            HomeItem,
            MoviesItem,
            ListsItem,
            UserItem
        )

        fun isMainRoute(route: Any?): Boolean {
            return route != null && items.any { it.route::class == route::class }
        }
    }
}
