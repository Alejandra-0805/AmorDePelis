package com.alejandra.amordepelis.features.user.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alejandra.amordepelis.features.user.presentation.components.PartnerSearchCard
import com.alejandra.amordepelis.features.user.presentation.components.UserHeader
import com.alejandra.amordepelis.features.user.presentation.viewmodels.UserViewModel

@Composable
fun PartnerSearchScreen(
    viewModel: UserViewModel = viewModel()
) {
    val uiState by viewModel.partnerSearchUiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            UserHeader(title = "Buscar pareja")
            Spacer(modifier = Modifier.height(20.dp))

            PartnerSearchCard(
                query = uiState.searchQuery,
                isLoading = uiState.isLoading,
                results = uiState.results,
                onQueryChange = viewModel::onSearchQueryChange,
                onSearchClick = viewModel::searchUsersByUsername,
                onInviteClick = viewModel::sendPartnerInvitation,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}
