package com.alejandra.amordepelis.features.auth.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alejandra.amordepelis.R
import com.alejandra.amordepelis.features.auth.presentation.components.AuthButton
import com.alejandra.amordepelis.features.auth.presentation.components.AuthErrorText
import com.alejandra.amordepelis.features.auth.presentation.components.AuthFooterLink
import com.alejandra.amordepelis.features.auth.presentation.components.AuthHeader
import com.alejandra.amordepelis.features.auth.presentation.components.AuthTextField
import com.alejandra.amordepelis.features.auth.presentation.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: AuthViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var email by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
            viewModel.clearState()
        }
    }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Header
                AuthHeader(
                    title = "¿Olvidaste tu contraseña?",
                    subtitle = "No te preocupes, ingresa tu correo y te enviaremos instrucciones para recuperarla"
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email field
                AuthTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        viewModel.clearError()
                    },
                    label = "Correo electrónico",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    isError = uiState.error != null,
                    enabled = !uiState.isLoading
                )

                // Error message
                uiState.error?.let { error ->
                    AuthErrorText(text = error)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Submit button
                AuthButton(
                    text = "Enviar instrucciones",
                    onClick = { viewModel.forgotPassword(email) },
                    isLoading = uiState.isLoading,
                    enabled = email.isNotBlank()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Info text
                Text(
                    text = "Te enviaremos un enlace a tu correo electrónico para que puedas restablecer tu contraseña de forma segura.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Back to login link
                AuthFooterLink(
                    questionText = "¿Recordaste tu contraseña?",
                    linkText = "Inicia sesión",
                    onLinkClick = onNavigateToLogin
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
