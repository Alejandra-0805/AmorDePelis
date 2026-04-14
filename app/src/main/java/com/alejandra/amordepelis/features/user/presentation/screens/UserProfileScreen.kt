package com.alejandra.amordepelis.features.user.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandra.amordepelis.features.user.presentation.components.PartnerSectionCard
import com.alejandra.amordepelis.features.user.presentation.components.UserHeader
import com.alejandra.amordepelis.features.user.presentation.components.UserInfoCard
import com.alejandra.amordepelis.features.user.presentation.viewmodels.UserViewModel

@Composable
fun UserProfileScreen(
    viewModel: UserViewModel = hiltViewModel(),
    onAddPartnerClick: () -> Unit = {}
) {
    val uiState by viewModel.profileUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    LaunchedEffect(uiState.message, uiState.error) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearProfileMessage()
        }
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearProfileMessage() // Using the same clear or let's assume we clear errors differently if we added it
        }
    }

    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showDeleteDialog(false) },
            title = { Text("Eliminar cuenta") },
            text = { Text("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.deleteProfile() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showDeleteDialog(false) }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            UserHeader(title = uiState.screenTitle)

            Spacer(modifier = Modifier.height(20.dp))

            UserInfoCard(
                username = uiState.username,
                email = uiState.email,
                passwordMasked = uiState.passwordMasked,
                isEditing = uiState.isEditing,
                onUsernameChange = viewModel::updateUsername,
                onEditClick = viewModel::toggleEditMode,
                onSaveClick = viewModel::saveProfile,
                onDeleteClick = { viewModel.showDeleteDialog(true) },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PartnerSectionCard(
                hasPartner = uiState.hasPartner,
                partnerUsername = uiState.partnerUsername,
                partnerEmail = uiState.partnerEmail,
                onAddPartnerClick = onAddPartnerClick,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}
