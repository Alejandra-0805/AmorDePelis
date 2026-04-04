package com.alejandra.amordepelis.features.lists.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.alejandra.amordepelis.features.lists.presentation.components.AddListFormCard
import com.alejandra.amordepelis.features.lists.presentation.components.ListsHeader
import com.alejandra.amordepelis.features.lists.presentation.viewmodels.ListsViewModel

@Composable
fun AddListScreen(
    viewModel: ListsViewModel = viewModel(),
    onSaved: () -> Unit = {}
) {
    val uiState by viewModel.addListUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSaved()
            viewModel.resetCreateListState()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ListsHeader(
                title = "Nuestra Cartelera",
                subtitle = "Persona 1 & Persona 2"
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = uiState.screenTitle,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))
            AddListFormCard(
                name = uiState.name,
                description = uiState.description,
                isLoading = uiState.isLoading,
                onNameChange = viewModel::onNewListNameChange,
                onDescriptionChange = viewModel::onNewListDescriptionChange,
                onSaveClick = viewModel::createList,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AddListScreenPreview() {
    AppTheme(dynamicColor = false) {
        Column(modifier = Modifier.fillMaxSize()) {
            ListsHeader(title = "Nuestra Cartelera", subtitle = "Persona 1 & Persona 2")
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Nueva lista",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            AddListFormCard(
                name = "",
                description = "",
                isLoading = false,
                onNameChange = {},
                onDescriptionChange = {},
                onSaveClick = {},
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}
