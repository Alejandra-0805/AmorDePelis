package com.alejandra.amordepelis.features.lists.presentation.screens

import com.alejandra.amordepelis.features.movies.domain.entities.Movie

data class SharedListItemUiModel(
    val id: String,
    val name: String,
    val description: String,
    val movieCount: Int,
    val colorHex: String
)

data class AnnouncementUiModel(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String? = null
)

data class ListsScreenUiState(
    val screenTitle: String = "Listas Compartidas",
    val subtitle: String = "Creen listas personalizadas para organizar sus peliculas juntos",
    val lists: List<SharedListItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    // Permisos basados en rol
    val canCreateLists: Boolean = false,
    val canEditLists: Boolean = false,
    val canDeleteLists: Boolean = false,
    // Estado de conectividad
    val isConnected: Boolean = true,
    val hasOfflineData: Boolean = false
)

data class AddListUiState(
    val screenTitle: String = "Nueva lista",
    val name: String = "",
    val description: String = "",
    val colorHex: String = "#3B82F6",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

data class ListDetailsUiState(
    val screenTitle: String = "Detalles de lista",
    val listId: String = "",
    val listName: String = "",
    val description: String = "",
    val colorHex: String = "#3B82F6",
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class EditListModalUiState(
    val isVisible: Boolean = false,
    val listId: String = "",
    val name: String = "",
    val description: String = "",
    val isSaving: Boolean = false,
    val error: String? = null
)

data class DeleteListModalUiState(
    val isVisible: Boolean = false,
    val listId: String = "",
    val listName: String = "",
    val isDeleting: Boolean = false,
    val error: String? = null
)
