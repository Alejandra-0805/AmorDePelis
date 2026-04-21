package com.alejandra.amordepelis.features.movies.presentation.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandra.amordepelis.core.hardware.ui.PhotoPreviewBox
import com.alejandra.amordepelis.core.hardware.ui.PhotoSelectionDialog
import com.alejandra.amordepelis.features.movies.presentation.components.MoviesTopHeader
import com.alejandra.amordepelis.features.movies.presentation.viewmodels.MoviesViewModel
import kotlinx.coroutines.launch

@Composable
fun AddMovieScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    onMovieSaved: () -> Unit = {}
) {
    val uiState by viewModel.addMovieUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showPhotoDialog by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<android.net.Uri?>(null) }
    var imageUriString by remember { mutableStateOf<String?>(null) }

    // ── Launchers ──────────────────────────────────────────────────────────────

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { imageUriString = it.toString() } }
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) tempCameraUri?.let { imageUriString = it.toString() }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.createCameraUri()?.let { uri ->
                tempCameraUri = uri
                takePictureLauncher.launch(uri)
            }
        } else {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "Permiso de cámara denegado",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    // ── Diálogo de selección de foto (composable reutilizable) ─────────────────

    PhotoSelectionDialog(
        isVisible = showPhotoDialog,
        title = "Seleccionar póster",
        onDismiss = { showPhotoDialog = false },
        onCameraClick = {
            showPhotoDialog = false
            if (viewModel.hasCameraPermission()) {
                viewModel.createCameraUri()?.let { uri ->
                    tempCameraUri = uri
                    takePictureLauncher.launch(uri)
                }
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        onGalleryClick = {
            showPhotoDialog = false
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    )

    // ── Efectos ───────────────────────────────────────────────────────────────

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onMovieSaved()
            viewModel.resetAddMovieState()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearAddMovieError()
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────────

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            MoviesTopHeader(
                title = "Nuestra Cartelera",
                subtitle = "Persona 1 & Persona 2"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Agregar película",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "Póster:", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))

                    // ── Composable reutilizable de core/hardware/ui ──────────
                    PhotoPreviewBox(
                        imageUri = imageUriString,
                        onTap = { showPhotoDialog = true },
                        placeholder = "Toca para subir una foto"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "Titulo:", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.movieTitle,
                        onValueChange = viewModel::updateAddMovieTitle,
                        placeholder = { Text("Nombre de la pelicula") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "Sinopsis", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.synopsis,
                        onValueChange = viewModel::updateAddMovieSynopsis,
                        placeholder = { Text("Descripcion de la pelicula") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        enabled = !uiState.isLoading,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "Calificacion:", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        (1..4).forEach { index ->
                            Icon(
                                imageVector = if (uiState.rating >= index) Icons.Default.Star else Icons.Outlined.StarOutline,
                                contentDescription = "Estrella $index",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable(enabled = !uiState.isLoading) {
                                        viewModel.updateAddMovieRating(index)
                                    },
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = uiState.isFavorite,
                            onCheckedChange = viewModel::updateAddMovieIsFavorite,
                            enabled = !uiState.isLoading,
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Marcar como favorita",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.addMovie(imageUriString) },
                        enabled = !uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Agregar pelicula", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
