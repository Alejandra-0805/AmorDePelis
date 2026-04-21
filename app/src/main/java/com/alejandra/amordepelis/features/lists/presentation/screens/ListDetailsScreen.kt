package com.alejandra.amordepelis.features.lists.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.alejandra.amordepelis.core.ui.theme.AppTheme
import com.alejandra.amordepelis.features.lists.presentation.components.ListsHeader
import com.alejandra.amordepelis.features.lists.presentation.viewmodels.ListsViewModel
import com.alejandra.amordepelis.features.movies.domain.entities.Movie

@Composable
fun ListDetailsScreen(
    listId: String,
    viewModel: ListsViewModel = hiltViewModel()
) {
    Log.d("ListDetailsScreen", "ListDetailsScreen initialized with listId=$listId")
    
    val uiState by viewModel.detailsUiState.collectAsStateWithLifecycle()
    Log.d("ListDetailsScreen", "UI State: $uiState")
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(listId) {
        Log.d("ListDetailsScreen", "LaunchedEffect triggered with listId=$listId")
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
    ) { _ ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ListsHeader(
                title = "Nuestra Cartelera",
                subtitle = "Persona 1 & Persona 2"
            )

            if (uiState.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // List details header with name and color indicator
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape),
                                    color = parseHexColor(uiState.colorHex)
                                ) {}

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = uiState.listName,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Detalles de lista",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                IconButton(onClick = { /* Edit action */ }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar lista",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                IconButton(onClick = { /* Delete action */ }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar lista",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    // Description section
                    item {
                        Text(
                            text = "DESCRIPCIÓN",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                        )
                    }

                    item {
                        Text(
                            text = uiState.description.ifEmpty { "Sin descripción" },
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Movies section
                    item {
                        Text(
                            text = "PELÍCULAS",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                        )
                    }

                    items(uiState.movies, key = { it.id }) { movie ->
                        MovieListCard(
                            movie = movie,
                            colorHex = uiState.colorHex,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }

                    if (uiState.movies.isEmpty()) {
                        item {
                            Text(
                                text = "No hay películas en esta lista",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieListCard(
    movie: Movie,
    colorHex: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            // Left color border
            Surface(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxSize(),
                color = parseHexColor(colorHex)
            ) {}

            // Movie content
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Movie image
                if (!movie.imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = movie.imageUrl,
                        contentDescription = "Movie poster for ${movie.title}",
                        modifier = Modifier
                            .size(100.dp, 100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                // Movie info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Rating and duration row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        if (movie.averageRating != null && movie.ratingCount > 0) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                modifier = Modifier.size(16.dp),
                                tint = Color.Yellow
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${movie.averageRating}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = " | ",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        if (movie.durationMinutes != null) {
                            Icon(
                                imageVector = Icons.Outlined.AccessTime,
                                contentDescription = "Duration",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${movie.durationMinutes}m",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Tags
                    if (movie.tags.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            movie.tags.take(1).forEach { tag ->
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
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

@Composable
private fun parseHexColor(hexColor: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hexColor))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ListDetailsScreenPreview() {
    AppTheme(dynamicColor = false) {
        val mockMovies = listOf(
            Movie(
                id = "1",
                title = "Interstellar",
                tags = listOf("Ciencia Ficción"),
                durationMinutes = 169,
                averageRating = 4.0,
                ratingCount = 1200
            ),
            Movie(
                id = "2",
                title = "Yo antes de ti",
                tags = listOf("Romance"),
                durationMinutes = 127,
                averageRating = 3.8,
                ratingCount = 800
            ),
            Movie(
                id = "3",
                title = "cars",
                tags = listOf("Aventura"),
                durationMinutes = 116,
                averageRating = 4.2,
                ratingCount = 950
            )
        )

        Column(modifier = Modifier.fillMaxSize()) {
            ListsHeader(title = "Nuestra Cartelera", subtitle = "Persona 1 & Persona 2")
            LazyColumn(modifier = Modifier.padding(20.dp)) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape),
                                color = Color(android.graphics.Color.parseColor("#E91E63"))
                            ) {}

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Mas tarde",
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = "Detalles de lista",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            IconButton(onClick = {}) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                            }

                            IconButton(onClick = {}) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "DESCRIPCIÓN",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Películas que queremos ver juntos",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = "PELÍCULAS",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(mockMovies) { movie ->
                    MovieListCard(movie, "#E91E63", Modifier.padding(bottom = 10.dp))
                }
            }
        }
    }
}
