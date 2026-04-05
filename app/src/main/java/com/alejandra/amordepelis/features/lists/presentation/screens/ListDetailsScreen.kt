package com.alejandra.amordepelis.features.lists.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandra.amordepelis.core.ui.theme.AppTheme
import com.alejandra.amordepelis.features.lists.presentation.components.ListsHeader
import com.alejandra.amordepelis.features.lists.presentation.viewmodels.ListsViewModel
import com.alejandra.amordepelis.features.movies.domain.entities.Movie

@Composable
fun ListDetailsScreen(
    listId: String,
    viewModel: ListsViewModel = hiltViewModel()
) {
    val uiState by viewModel.detailsUiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(listId) {
        viewModel.loadListDetails(listId)
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ListsHeader(
                title = "Nuestra Cartelera",
                subtitle = "Persona 1 & Persona 2"
            )

            if (uiState.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.weight(1f))
                }
            } else {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = uiState.listName,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = uiState.screenTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Text(text = "DESCRIPCION", style = MaterialTheme.typography.labelLarge)
                    Text(text = uiState.description, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                    Text(text = "PELICULAS", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.padding(top = 8.dp))

                    LazyColumn {
                        items(uiState.movies, key = { it.id }) { movie ->
                            Card(
                                modifier = Modifier.padding(bottom = 10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = movie.title,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    androidx.compose.foundation.layout.Row {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = " ${movie.rating}",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "  |  ",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = " ${movie.durationMinutes}m",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ListDetailsScreenPreview() {
    AppTheme(dynamicColor = false) {
        val mockMovies = listOf(
            Movie(
                id = "1",
                title = "Titanic",
                synopsis = "",
                genre = "Romance",
                durationMinutes = 127,
                rating = 4,
                isFavorite = false
            ),
            Movie(
                id = "2",
                title = "Yo antes de ti",
                synopsis = "",
                genre = "Romance",
                durationMinutes = 127,
                rating = 4,
                isFavorite = false
            )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            ListsHeader(title = "Nuestra Cartelera", subtitle = "Persona 1 & Persona 2")
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Para ver", style = MaterialTheme.typography.headlineLarge)
                Text(text = "Detalles de lista", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Text(text = "DESCRIPCION", style = MaterialTheme.typography.labelLarge)
                Text(text = "Peliculas que queremos ver juntos", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Text(text = "PELICULAS", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.padding(top = 8.dp))
                mockMovies.forEach { movie ->
                    Card(modifier = Modifier.padding(bottom = 10.dp)) {
                        Text(text = movie.title, modifier = Modifier.padding(14.dp))
                    }
                }
            }
        }
    }
}
