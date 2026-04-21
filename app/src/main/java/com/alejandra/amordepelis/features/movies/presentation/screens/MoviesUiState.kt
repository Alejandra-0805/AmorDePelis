package com.alejandra.amordepelis.features.movies.presentation.screens

import com.alejandra.amordepelis.features.movies.domain.entities.Movie

/**
 * Estado de la pantalla de lista de películas.
 *
 * MVVM - UI State immutable:
 *   Cada cambio genera una nueva copia via [copy], nunca se muta directamente.
 *
 * Offline-First UI flags:
 *   - [isConnected]   → indica si hay red en este momento.
 *   - [isSyncing]     → true mientras se descarga del servidor en background.
 *   - [hasOfflineData]→ true si los datos que se muestran provienen del cache
 *                       local (sin sincronización exitosa aún).
 *   - [syncError]     → mensaje de error de sincronización (no bloquea la UI).
 */
data class MoviesListUiState(
    val title: String = "Películas",
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null,

    // Permisos basados en rol
    val canAddMoviesToCatalog: Boolean = false,
    val canAddToPersonalLists: Boolean = false,
    val canMarkAsWatched: Boolean = false,
    val canMarkAsFavorite: Boolean = false,

    // Estado offline-first
    val isConnected: Boolean = true,
    val isSyncing: Boolean = false,
    val hasOfflineData: Boolean = false,
    val syncError: String? = null
)

data class MovieDetailsUiState(
    val title: String = "Detalles de película",
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: String? = null
)

/**
 * Estado del formulario de agregar película.
 * Nota: separado de [MoviesListUiState] siguiendo Single Responsibility.
 *
 * Los campos de texto se almacenan como String para facilitar el binding
 * con OutlinedTextField (la conversión numérica se hace en el ViewModel).
 */
data class AddMovieUiState(
    val title: String = "Agregar película",
    // Campos del formulario
    val movieTitle: String = "",
    val synopsis: String = "",
    val durationMinutes: String = "",
    val tagsInput: String = "",        // Tags separados por coma, ej. "Acción, Drama"
    val imageUri: String? = null,      // URI de la imagen seleccionada
    // Errores de validación por campo
    val titleError: String? = null,
    val durationError: String? = null,
    // Campos heredados (usados por MoviesViewModel legacy)
    val genre: String = "",
    val rating: Int = 0,
    val isFavorite: Boolean = false,
    // Estado de la operación
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)


data class Announcement(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String
)
