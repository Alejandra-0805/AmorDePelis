package com.alejandra.amordepelis.features.movies.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alejandra.amordepelis.features.movies.presentation.components.MovieFormCard
import com.alejandra.amordepelis.features.movies.presentation.components.MoviesTopHeader
import com.alejandra.amordepelis.features.movies.presentation.viewmodels.MoviesViewModel

@Composable
fun AddMovieScreen(
    viewModel: MoviesViewModel = viewModel(),
    onMovieSaved: () -> Unit = {}
) {
    val uiState by viewModel.addUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onMovieSaved()
            viewModel.resetAddMovieState()
        }
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

            MovieFormCard(
                title = uiState.movieTitle,
                synopsis = uiState.synopsis,
                genre = uiState.genre,
                duration = uiState.durationMinutes,
                isFavorite = uiState.isFavorite,
                isLoading = uiState.isLoading,
                onTitleChange = viewModel::onTitleChange,
                onSynopsisChange = viewModel::onSynopsisChange,
                onGenreChange = viewModel::onGenreChange,
                onDurationChange = viewModel::onDurationChange,
                onFavoriteChange = viewModel::onFavoriteChange,
                onSubmit = viewModel::addMovie,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}
