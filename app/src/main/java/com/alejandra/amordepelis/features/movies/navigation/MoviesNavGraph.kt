package com.alejandra.amordepelis.features.movies.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.alejandra.amordepelis.core.navigation.AddMovie
import com.alejandra.amordepelis.core.navigation.FeatureNavGraph
import com.alejandra.amordepelis.core.navigation.MovieDetails
import com.alejandra.amordepelis.core.navigation.Movies
import com.alejandra.amordepelis.core.storage.SessionManager
import com.alejandra.amordepelis.features.movies.presentation.screens.AddMovieScreen
import com.alejandra.amordepelis.features.movies.presentation.screens.MovieDetailScreen
import com.alejandra.amordepelis.features.movies.presentation.screens.MoviesListScreen
import javax.inject.Inject

class MoviesNavGraph @Inject constructor(
    private val sessionManager: SessionManager
) : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Movies> {
            MoviesListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(MovieDetails)
                },
                onAddMovieClick = {
                    navController.navigate(AddMovie)
                }
            )
        }

        navGraphBuilder.composable<MovieDetails> {
            MovieDetailScreen(
                movieId = "" // TODO: Pass actual movieId when implementing navigation with arguments
            )
        }

        navGraphBuilder.composable<AddMovie> {
            // Guard: Solo ADMIN puede acceder a agregar películas al catálogo
            if (!sessionManager.canAddMoviesToCatalog()) {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            } else {
                AddMovieScreen(
                    onMovieSaved = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
