package com.alejandra.amordepelis.features.lists.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandra.amordepelis.core.ui.theme.AppTheme
import com.alejandra.amordepelis.features.lists.presentation.components.AddListFormCard
import com.alejandra.amordepelis.features.lists.presentation.components.ListsHeader
import com.alejandra.amordepelis.features.lists.presentation.viewmodels.ListsViewModel

@Composable
fun AddListScreen(
    viewModel: ListsViewModel = hiltViewModel(),
    onSaved: () -> Unit = {}
) {
    val uiState by viewModel.addListUiState.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onSaved()
            viewModel.resetCreateListState()
        }
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
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
