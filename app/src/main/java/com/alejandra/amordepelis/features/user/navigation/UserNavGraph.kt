package com.alejandra.amordepelis.features.user.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.alejandra.amordepelis.core.navigation.FeatureNavGraph
import com.alejandra.amordepelis.core.navigation.User
import com.alejandra.amordepelis.features.user.presentation.screens.UserProfileScreen
import javax.inject.Inject

class UserNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<User> {
            UserProfileScreen(
                onAddPartnerClick = {
                    // TODO: Navigate to add partner screen when implemented
                }
            )
        }
    }
}
