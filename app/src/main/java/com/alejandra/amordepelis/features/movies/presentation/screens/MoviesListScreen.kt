package com.alejandra.amordepelis.features.movies.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.alejandra.amordepelis.core.hardware.ui.rememberShakeDetector
import com.alejandra.amordepelis.features.lists.presentation.viewmodels.ListsViewModel
import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.presentation.components.AnnouncementCarousel
import com.alejandra.amordepelis.features.movies.presentation.components.MovieListItemCard
import com.alejandra.amordepelis.features.movies.presentation.components.MoviesTopHeader
import com.alejandra.amordepelis.features.movies.presentation.viewmodels.MoviesViewModel

@Composable
fun MoviesListScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    listsViewModel: ListsViewModel = hiltViewModel(),
    onMovieClick: (String) -> Unit = {},
    onAddMovieClick: () -> Unit = {}
) {
    val uiState by viewModel.listUiState.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val currentAnnouncementIndex by viewModel.currentAnnouncementIndex.collectAsStateWithLifecycle()

    val listMessage by listsViewModel.message.collectAsStateWithLifecycle()
    val listError by listsViewModel.error.collectAsStateWithLifecycle()
    val listsUiState by listsViewModel.listsUiState.collectAsStateWithLifecycle()
    val movieIdPendingAdd by listsViewModel.movieIdPendingAdd.collectAsStateWithLifecycle()
    val isAddingMovieToList by listsViewModel.isAddingMovieToList.collectAsStateWithLifecycle()
    val listIdsContainingPendingMovie by listsViewModel.listIdsContainingPendingMovie.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var shakenRandomMovie by remember { mutableStateOf<Movie?>(null) }
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(listMessage) {
        listMessage?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            listsViewModel.clearMessage()
        }
    }

    LaunchedEffect(listError) {
        listError?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            listsViewModel.clearError()
        }
    }

    LaunchedEffect(uiState.syncError) {
        uiState.syncError?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearSyncError()
        }
    }

    rememberShakeDetector(
        manager = viewModel.accelerometerManager,
        onShake = {
            if (uiState.movies.isNotEmpty() && shakenRandomMovie == null) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                shakenRandomMovie = uiState.movies.randomOrNull()
            }
        }
    )

    if (shakenRandomMovie != null) {
        val movie = shakenRandomMovie!!
        AlertDialog(
            onDismissRequest = { shakenRandomMovie = null },
            title = { Text("¡Película al Azar!", style = MaterialTheme.typography.titleLarge) },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Has agitado el teléfono. ¿Qué te parece ver:")
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (!movie.imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = movie.imageUrl,
                            contentDescription = movie.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    shakenRandomMovie = null
                    onMovieClick(movie.id)
                }) { Text("Ver Detalles") }
            },
            dismissButton = {
                TextButton(onClick = { shakenRandomMovie = null }) { Text("Cancelar") }
            }
        )
    }

    if (movieIdPendingAdd != null) {
        AlertDialog(
            onDismissRequest = { listsViewModel.dismissAddMovieToList() },
            title = { Text("Agregar a una lista") },
            text = {
                if (listsUiState.lists.isEmpty()) {
                    Text("No tienes listas disponibles. Crea una lista primero.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Elige la lista donde quieres guardar esta película:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        listsUiState.lists.forEach { list ->
                            val alreadyContains = list.id in listIdsContainingPendingMovie
                            TextButton(
                                onClick = { listsViewModel.confirmAddMovieToList(list.id) },
                                enabled = !isAddingMovieToList && !alreadyContains,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (alreadyContains) "${list.name} (ya agregada)" else list.name,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { listsViewModel.dismissAddMovieToList() },
                    enabled = !isAddingMovieToList
                ) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (uiState.canAddMoviesToCatalog) {
                FloatingActionButton(
                    onClick = onAddMovieClick,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar película"
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
    ) { _ ->
        Column(modifier = Modifier.fillMaxSize()) {

            MoviesTopHeader(
                title = "Nuestra Cartelera",
                subtitle = "Persona 1 & Persona 2"
            )

            AnimatedVisibility(
                visible = !uiState.isConnected,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                OfflineBanner()
            }

            AnimatedVisibility(
                visible = uiState.isSyncing,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                SyncingIndicator()
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnnouncementCarousel(
                announcements = announcements,
                currentIndex = currentAnnouncementIndex,
                onIndexChange = viewModel::onAnnouncementIndexChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = uiState.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading && uiState.movies.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.movies.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (!uiState.isConnected)
                                "Sin conexión y sin películas guardadas"
                            else
                                "No hay películas disponibles",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.movies, key = { it.id }) { movie ->
                            MovieListItemCard(
                                movie = movie,
                                canMarkAsFavorite = uiState.canMarkAsFavorite,
                                canMarkAsWatched = uiState.canMarkAsWatched,
                                canAddToList = uiState.canAddToPersonalLists,
                                onMovieClick = { onMovieClick(movie.id) },
                                onAddToListClick = { listsViewModel.requestAddMovieToList(movie.id) },
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OfflineBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = "Sin conexión — mostrando películas guardadas",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
private fun SyncingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(14.dp),
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Icon(
            imageVector = Icons.Default.Sync,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Sincronizando películas…",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
