package com.alejandra.amordepelis.features.movies.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandra.amordepelis.features.movies.presentation.components.AnnouncementCarousel
import com.alejandra.amordepelis.features.movies.presentation.components.MovieListItemCard
import com.alejandra.amordepelis.features.movies.presentation.components.MoviesTopHeader
import com.alejandra.amordepelis.features.movies.presentation.viewmodels.MoviesViewModel

@Composable
fun MoviesListScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    onMovieClick: (String) -> Unit = {},
    onAddMovieClick: () -> Unit = {}
) {
    val uiState by viewModel.listUiState.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val currentAnnouncementIndex by viewModel.currentAnnouncementIndex.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMovies()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            // Solo mostrar FAB si el usuario puede agregar películas al catálogo (ADMIN)
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MoviesTopHeader(
                title = "Nuestra Cartelera",
                subtitle = "Persona 1 & Persona 2"
            )

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

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.movies.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay películas disponibles",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
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
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }
            }
        }
    }
}
