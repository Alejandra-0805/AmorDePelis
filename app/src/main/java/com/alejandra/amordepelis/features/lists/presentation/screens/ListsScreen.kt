package com.alejandra.amordepelis.features.lists.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alejandra.amordepelis.core.ui.theme.AppTheme
import com.alejandra.amordepelis.features.lists.presentation.components.DeleteListDialog
import com.alejandra.amordepelis.features.lists.presentation.components.EditListDialog
import com.alejandra.amordepelis.features.lists.presentation.components.ListsHeader
import com.alejandra.amordepelis.features.lists.presentation.components.SharedListCard
import com.alejandra.amordepelis.features.lists.presentation.viewmodels.ListsViewModel

@Composable
fun ListsScreen(
    viewModel: ListsViewModel = viewModel(),
    onAddNewListClick: () -> Unit = {},
    onListClick: (String) -> Unit = {}
) {
    val uiState by viewModel.listsUiState.collectAsStateWithLifecycle()
    val editModal by viewModel.editModalUiState.collectAsStateWithLifecycle()
    val deleteModal by viewModel.deleteModalUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadSharedLists()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNewListClick) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ListsHeader(
                title = "Nuestra Cartelera",
                subtitle = "Persona 1 & Persona 2"
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = uiState.screenTitle,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = uiState.subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.lists, key = { it.id }) { item ->
                        SharedListCard(
                            item = item,
                            onEditClick = { viewModel.openEditModal(item) },
                            onDeleteClick = { viewModel.openDeleteModal(item) },
                            modifier = Modifier
                                .padding(bottom = 2.dp)
                        )
                    }
                }
            }
        }
    }

    if (editModal.isVisible) {
        EditListDialog(
            name = editModal.name,
            description = editModal.description,
            onNameChange = viewModel::onEditNameChange,
            onDescriptionChange = viewModel::onEditDescriptionChange,
            onDismiss = viewModel::closeEditModal,
            onConfirm = viewModel::saveListEdition
        )
    }

    if (deleteModal.isVisible) {
        DeleteListDialog(
            listName = deleteModal.listName,
            onDismiss = viewModel::closeDeleteModal,
            onConfirm = viewModel::deleteList
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ListsScreenPreview() {
    AppTheme(dynamicColor = false) {
        val state = ListsScreenUiState(
            lists = listOf(
                SharedListItemUiModel(
                    id = "1",
                    name = "Para ver",
                    description = "Peliculas que queremos ver juntos",
                    movieCount = 0,
                    colorHex = "#3B82F6"
                ),
                SharedListItemUiModel(
                    id = "2",
                    name = "Favoritas",
                    description = "Nuestras peliculas favoritas",
                    movieCount = 0,
                    colorHex = "#EF4444"
                )
            )
        )

        Column {
            ListsHeader(title = "Nuestra Cartelera", subtitle = "Persona 1 & Persona 2")
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = state.screenTitle, style = MaterialTheme.typography.headlineLarge)
                Text(text = state.subtitle, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(14.dp))
                state.lists.forEach { item ->
                    SharedListCard(
                        item = item,
                        onEditClick = {},
                        onDeleteClick = {}
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
