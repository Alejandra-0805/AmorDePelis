package com.alejandra.amordepelis.features.lists.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.alejandra.amordepelis.core.navigation.AddList
import com.alejandra.amordepelis.core.navigation.FeatureNavGraph
import com.alejandra.amordepelis.core.navigation.Lists
import com.alejandra.amordepelis.core.navigation.ListDetails
import com.alejandra.amordepelis.features.lists.presentation.screens.AddListScreen
import com.alejandra.amordepelis.features.lists.presentation.screens.ListDetailsScreen
import com.alejandra.amordepelis.features.lists.presentation.screens.ListsScreen
import javax.inject.Inject

class ListsNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Lists> {
            ListsScreen(
                onListClick = { listId ->
                    navController.navigate(ListDetails)
                },
                onAddNewListClick = {
                    navController.navigate(AddList)
                }
            )
        }

        navGraphBuilder.composable<ListDetails> {
            ListDetailsScreen(
                listId = "" // TODO: Pass actual listId when implementing navigation with arguments
            )
        }

        navGraphBuilder.composable<AddList> {
            AddListScreen(
                onSaved = {
                    navController.popBackStack()
                }
            )
        }
    }
}