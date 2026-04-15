package com.alejandra.amordepelis.features.lists.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alejandra.amordepelis.core.navigation.AddList
import com.alejandra.amordepelis.core.navigation.FeatureNavGraph
import com.alejandra.amordepelis.core.navigation.Lists
import com.alejandra.amordepelis.core.navigation.ListDetails
import com.alejandra.amordepelis.features.lists.presentation.screens.AddListScreen
import com.alejandra.amordepelis.features.lists.presentation.screens.ListDetailsScreen
import com.alejandra.amordepelis.features.lists.presentation.screens.ListsScreen
import com.alejandra.amordepelis.features.lists.presentation.viewmodels.ListsViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import javax.inject.Inject

class ListsNavGraph @Inject constructor() : FeatureNavGraph {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable<Lists> { backStackEntry ->
            val viewModel: ListsViewModel = hiltViewModel()
            val savedStateHandle = backStackEntry.savedStateHandle
            val shouldRefresh = savedStateHandle.getStateFlow("refreshLists", false)
                .collectAsStateWithLifecycle()

            LaunchedEffect(shouldRefresh.value) {
                if (shouldRefresh.value) {
                    viewModel.loadSharedLists()
                    savedStateHandle["refreshLists"] = false
                }
            }

            ListsScreen(
                viewModel = viewModel,
                onListClick = { listId ->
                    navController.navigate(ListDetails(listId))
                },
                onAddNewListClick = {
                    navController.navigate(AddList)
                }
            )
        }

        navGraphBuilder.composable<ListDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<ListDetails>()
            ListDetailsScreen(
                listId = args.listId
            )
        }

        navGraphBuilder.composable<AddList> {
            val parentEntry = navController.previousBackStackEntry
            val sharedViewModel: ListsViewModel = if (parentEntry != null) {
                hiltViewModel(parentEntry)
            } else {
                hiltViewModel()
            }

            AddListScreen(
                viewModel = sharedViewModel,
                onSaved = {
                    navController.popBackStack()
                }
            )
        }
    }
}