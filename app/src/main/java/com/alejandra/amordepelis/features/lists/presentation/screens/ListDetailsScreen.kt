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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alejandra.amordepelis.core.ui.theme.AppTheme
import com.alejandra.amordepelis.features.lists.presentation.components.ListsHeader
import com.alejandra.amordepelis.features.lists.presentation.viewmodels.ListsViewModel
import com.alejandra.amordepelis.features.movies.domain.entities.Movie

@Composable
fun ListDetailsScreen(
    listId: String,
    viewModel: ListsViewModel = viewModel()
) {
    val uiState by viewModel.detailsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(listId) {
        viewModel.loadListDetails(listId)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ListsHeader(
                title = "Nuestra Cartelera",
                subtitle = "Persona 1 & Persona 2"
            )

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
