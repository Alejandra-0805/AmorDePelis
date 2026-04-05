package com.alejandra.amordepelis.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.alejandra.amordepelis.features.user.navigation.UserNavGraph

@Composable
fun NavigationWrapper (
    navGraphs: Set<FeatureNavGraph>,
    startDestination: Any = Login
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navGraphs.forEach { graph ->
            graph.registerGraph(this, navController)
        }
    }
}