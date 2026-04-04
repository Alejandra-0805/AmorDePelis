package com.alejandra.amordepelis.features.lists.presentation.navigation

sealed class ListsRoutes(val route: String) {
    data object Lists : ListsRoutes("lists/all")
    data object AddList : ListsRoutes("lists/add")

    data object ListDetails : ListsRoutes("lists/detail/{listId}") {
        fun createRoute(listId: String): String = "lists/detail/$listId"
    }
}
