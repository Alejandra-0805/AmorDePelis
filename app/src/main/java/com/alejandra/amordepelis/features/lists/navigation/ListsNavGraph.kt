package com.alejandra.amordepelis.features.lists.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.alejandra.amordepelis.core.navigation.AddList
import com.alejandra.amordepelis.core.navigation.FeatureNavGraph
import com.alejandra.amordepelis.core.navigation.Lists
import com.alejandra.amordepelis.core.navigation.ListDetails
import javax.inject.Inject

class ListsNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Lists> {  }

        navGraphBuilder.composable<ListDetails> {  }

        navGraphBuilder.composable<AddList> {  }
    }
}