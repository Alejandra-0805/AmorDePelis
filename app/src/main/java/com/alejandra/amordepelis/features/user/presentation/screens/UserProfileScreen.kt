package com.alejandra.amordepelis.features.user.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandra.amordepelis.features.user.presentation.components.PartnerSectionCard
import com.alejandra.amordepelis.features.user.presentation.components.UserAnnouncementCarousel
import com.alejandra.amordepelis.features.user.presentation.components.UserHeader
import com.alejandra.amordepelis.features.user.presentation.components.UserInfoCard
import com.alejandra.amordepelis.features.user.presentation.viewmodels.UserViewModel

@Composable
fun UserProfileScreen(
    viewModel: UserViewModel = hiltViewModel(),
    onAddPartnerClick: () -> Unit = {}
) {
    val uiState by viewModel.profileUiState.collectAsStateWithLifecycle()
    val announcements by viewModel.announcements.collectAsStateWithLifecycle()
    val currentAnnouncementIndex by viewModel.currentAnnouncementIndex.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            UserHeader(title = uiState.screenTitle)

            Spacer(modifier = Modifier.height(16.dp))

            UserAnnouncementCarousel(
                announcements = announcements,
                currentIndex = currentAnnouncementIndex,
                onIndexChange = viewModel::onAnnouncementIndexChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            UserInfoCard(
                username = uiState.username,
                email = uiState.email,
                passwordMasked = uiState.passwordMasked,
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
