package com.alejandra.amordepelis.features.movies.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandra.amordepelis.core.ui.theme.AppTheme
import com.alejandra.amordepelis.features.movies.presentation.components.MovieFormCard
import com.alejandra.amordepelis.features.movies.presentation.components.MoviesTopHeader
import com.alejandra.amordepelis.features.movies.presentation.viewmodels.MoviesViewModel

@Composable
fun AddMovieScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    onMovieSaved: () -> Unit = {}
) {
    val uiState by viewModel.addUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            snackbarHostState.showSnackbar(
                message = "¡Película agregada exitosamente!",
                duration = SnackbarDuration.Short
            )
            onMovieSaved()
            viewModel.resetAddMovieState()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
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

            MovieFormCard(
                title = uiState.movieTitle,
                synopsis = uiState.synopsis,
                genre = uiState.genre,
                duration = uiState.durationMinutes,
                rating = uiState.rating,
                isFavorite = uiState.isFavorite,
                isLoading = uiState.isLoading,
                error = uiState.error,
                onTitleChange = viewModel::onTitleChange,
                onSynopsisChange = viewModel::onSynopsisChange,
                onGenreChange = viewModel::onGenreChange,
                onDurationChange = viewModel::onDurationChange,
                onRatingChange = viewModel::onRatingChange,
                onFavoriteChange = viewModel::onFavoriteChange,
                onSubmit = viewModel::addMovie,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddMovieScreenPreview() {
    AppTheme(dynamicColor = false) {
        Column(modifier = Modifier.fillMaxSize()) {
            MoviesTopHeader(
                title = "Nuestra Cartelera",
                subtitle = "Persona 1 & Persona 2"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Agregar película",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            MovieFormCard(
                title = "",
                synopsis = "",
                genre = "",
                duration = "",
                rating = 0,
                isFavorite = false,
                isLoading = false,
                error = null,
                onTitleChange = {},
                onSynopsisChange = {},
                onGenreChange = {},
                onDurationChange = {},
                onRatingChange = {},
                onFavoriteChange = {},
                onSubmit = {},
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}
