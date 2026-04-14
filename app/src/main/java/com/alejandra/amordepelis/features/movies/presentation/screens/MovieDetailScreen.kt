package com.alejandra.amordepelis.features.movies.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandra.amordepelis.features.movies.presentation.components.MoviesTopHeader
import com.alejandra.amordepelis.features.movies.presentation.viewmodels.MoviesViewModel

@Composable
fun MovieDetailScreen(
    movieId: String,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val uiState by viewModel.detailsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MoviesTopHeader(
                title = "Nuestra Cartelera",
                subtitle = "Persona 1 & Persona 2"
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = uiState.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(horizontal = 20.dp))
            } else {
                uiState.movie?.let { movie ->
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        if (movie.tags.isNotEmpty()) {
                            Text(
                                text = movie.tags.joinToString(" · "),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            }
        }
    }
}
