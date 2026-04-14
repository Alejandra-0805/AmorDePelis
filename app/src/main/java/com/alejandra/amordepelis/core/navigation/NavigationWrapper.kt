package com.alejandra.amordepelis.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alejandra.amordepelis.core.storage.SessionManager
import com.alejandra.amordepelis.core.ui.components.AppBottomNavigationBar

@Composable
fun NavigationWrapper(
    navGraphs: Set<FeatureNavGraph>,
    sessionManager: SessionManager,
    startDestination: Any = Login
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRole by sessionManager.currentRole.collectAsStateWithLifecycle()
    val isLoggedIn by sessionManager.isLoggedIn.collectAsStateWithLifecycle()

    // Determinar startDestination dinámico basado en estado de sesión
    val resolvedStartDestination: Any = if (isLoggedIn) Home else Login

    // Auth Guard: redirigir a Login si el usuario cierra sesión mid-session
    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            val currentRoute = navController.currentDestination?.route ?: ""
            val isOnAuthScreen = currentRoute.contains(Login::class.qualifiedName ?: "") ||
                    currentRoute.contains(Register::class.qualifiedName ?: "")
            if (!isOnAuthScreen) {
                navController.navigate(Login) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    // Auth Guard: si ya está autenticado y está en Login, redirigir a Home
    LaunchedEffect(isLoggedIn, currentDestination) {
        if (isLoggedIn) {
            val currentRoute = currentDestination?.route ?: ""
            if (currentRoute.contains(Login::class.qualifiedName ?: "")) {
                navController.navigate(Home) {
                    popUpTo(Login) { inclusive = true }
                }
            }
        }
    }

    // Filtrar items de navegación según el rol del usuario
    val visibleItems = BottomNavItem.getItemsForRole(currentRole)

    val showBottomBar = currentDestination?.route?.let { route ->
        BottomNavItem.allItems.any { item ->
            route.contains(item.route::class.qualifiedName ?: "")
        }
    } ?: false

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(
                    items = visibleItems,
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
            startDestination = resolvedStartDestination,
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