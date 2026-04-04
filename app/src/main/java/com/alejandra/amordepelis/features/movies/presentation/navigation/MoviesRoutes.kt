package com.alejandra.amordepelis.features.movies.presentation.navigation

sealed class MoviesRoutes(val route: String) {
    data object MoviesList : MoviesRoutes("movies/list")
    data object AddMovie : MoviesRoutes("movies/add")

    data object MovieDetail : MoviesRoutes("movies/detail/{movieId}") {
        fun createRoute(movieId: String): String = "movies/detail/$movieId"
    }
}
