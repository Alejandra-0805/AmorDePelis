package com.alejandra.amordepelis.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alejandra.amordepelis.core.ui.components.AppBottomNavigationBar

@Composable
fun NavigationWrapper(
    navGraphs: Set<FeatureNavGraph>,
    startDestination: Any = Login
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route?.let { route ->
        BottomNavItem.items.any { item ->
            route.contains(item.route::class.qualifiedName ?: "")
        }
    } ?: false

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(
                    items = BottomNavItem.items,
                    currentRoute = getCurrentRoute(currentDestination?.route),
                    onNavigate = { item ->
                        navController.navigate(item.route) {
                            popUpTo(Home) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            navGraphs.forEach { graph ->
                graph.registerGraph(this, navController)
            }
        }
    }
}

private fun getCurrentRoute(route: String?): Any? {
    if (route == null) return null
    return when {
        route.contains(Home::class.qualifiedName ?: "") -> Home
        route.contains(Movies::class.qualifiedName ?: "") -> Movies
        route.contains(Lists::class.qualifiedName ?: "") -> Lists
        route.contains(User::class.qualifiedName ?: "") -> User
        else -> null
    }
}