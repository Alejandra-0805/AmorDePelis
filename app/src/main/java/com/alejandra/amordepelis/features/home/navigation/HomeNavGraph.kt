package com.alejandra.amordepelis.features.home.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.alejandra.amordepelis.core.navigation.AddAnnouncement
import com.alejandra.amordepelis.core.navigation.FeatureNavGraph
import com.alejandra.amordepelis.core.navigation.Home
import com.alejandra.amordepelis.core.storage.SessionManager
import com.alejandra.amordepelis.features.home.presentation.screens.AddAnnouncementScreen
import com.alejandra.amordepelis.features.home.presentation.screens.HomeScreen
import javax.inject.Inject

class HomeNavGraph @Inject constructor(
    private val sessionManager: SessionManager
) : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Home> {
            HomeScreen()
        }

        navGraphBuilder.composable<AddAnnouncement> {
            // Guard: solo ADMIN puede crear anuncios
            if (!sessionManager.canAddNews()) {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            } else {
                AddAnnouncementScreen(
                    onSaved = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
