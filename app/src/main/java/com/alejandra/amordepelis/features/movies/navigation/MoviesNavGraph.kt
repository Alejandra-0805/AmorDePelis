package com.alejandra.amordepelis.features.movies.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.alejandra.amordepelis.core.navigation.AddMovie
import com.alejandra.amordepelis.core.navigation.FeatureNavGraph
import com.alejandra.amordepelis.core.navigation.MovieDetails
import com.alejandra.amordepelis.core.navigation.Movies
import javax.inject.Inject

class MoviesNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Movies> {  }

        navGraphBuilder.composable<MovieDetails> {  }

        navGraphBuilder.composable<AddMovie> {  }
    }
}
