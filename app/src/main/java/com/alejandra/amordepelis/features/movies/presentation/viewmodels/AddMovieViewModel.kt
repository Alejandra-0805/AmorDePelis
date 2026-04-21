package com.alejandra.amordepelis.features.movies.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.core.hardware.domain.CameraManager
import com.alejandra.amordepelis.core.hardware.domain.HapticFeedbackManager
import com.alejandra.amordepelis.features.movies.domain.entities.MovieFormData
import com.alejandra.amordepelis.features.movies.domain.usecases.CreateMovieWithImageUseCase
import com.alejandra.amordepelis.features.movies.presentation.screens.AddMovieUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * ViewModel dedicado para la pantalla de agregar película.
 *
 * SOLID:
 * - SRP: mantiene estado de UI + orquesta use cases (no mezcla otras features)
 * - DIP: depende de interfaces (CameraManager, HapticFeedbackManager, use case)
 * - OCP: agregar campos nuevos no rompe la estructura existente
 *
 * Los ActivityResultLauncher viven en el Composable (requieren Activity lifecycle);
 * este ViewModel solo coordina CameraManager para crear URIs y verificar permisos.
 */
@HiltViewModel
class AddMovieViewModel @Inject constructor(
    private val createMovieUseCase: CreateMovieWithImageUseCase,
    private val cameraManager: CameraManager,
    private val hapticFeedbackManager: HapticFeedbackManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    companion object {
        private const val TAG = "AddMovieViewModel"
    }

    private val _uiState = MutableStateFlow(AddMovieUiState())
    val uiState: StateFlow<AddMovieUiState> = _uiState.asStateFlow()

    // ============= FORM FIELD UPDATES =============

    fun updateTitle(value: String) {
        _uiState.update { it.copy(movieTitle = value, titleError = validateTitle(value)) }
    }

    fun updateSynopsis(value: String) {
        _uiState.update { it.copy(synopsis = value) }
    }

    /** Solo acepta cadenas numéricas; ignora cualquier otro carácter. */
    fun updateDuration(value: String) {
        if (value.isEmpty() || value.all { it.isDigit() }) {
            _uiState.update { it.copy(durationMinutes = value, durationError = validateDuration(value)) }
        }
    }

    /** Tags separados por coma; ej. "Acción, Drama, Romance". */
    fun updateTags(value: String) {
        _uiState.update { it.copy(tagsInput = value) }
    }

    /** Llamado desde el Composable cuando el launcher devuelve un URI. */
    fun updateImageUri(uri: String?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    // ============= CAMERA HELPERS (sin lanzar launchers) =============

    /** Crea un URI temporal para la cámara; devuelve null si FileProvider falla. */
    fun createCameraUri(): Uri? = cameraManager.createTempCameraUri()

    /** Verifica si el permiso de cámara está concedido. */
    fun hasCameraPermission(): Boolean = cameraManager.hasCameraPermission()

    // ============= FORM SUBMISSION =============

    fun submitMovie() {
        val state = _uiState.value

        // Validación antes de enviar
        val titleErr = validateTitle(state.movieTitle)
        val durationErr = validateDuration(state.durationMinutes)

        if (titleErr != null || durationErr != null) {
            _uiState.update { it.copy(titleError = titleErr, durationError = durationErr) }
            return
        }

        val formData = buildFormData(state)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            var tempFile: File? = null
            try {
                if (state.imageUri != null) {
                    tempFile = copyUriToTempFile(state.imageUri)
                }

                val result = createMovieUseCase(formData, tempFile)

                result
                    .onSuccess { movie ->
                        Log.d(TAG, "Película creada: ${movie.title}")
                        hapticFeedbackManager.vibrateForNotification()
                        _uiState.update { it.copy(isLoading = false, isSaved = true) }
                    }
                    .onFailure { error ->
                        Log.e(TAG, "Error al crear película", error)
                        _uiState.update {
                            it.copy(isLoading = false, error = error.message ?: "Error desconocido")
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al procesar imagen", e)
                _uiState.update { it.copy(isLoading = false, error = "Error al procesar la imagen") }
            } finally {
                tempFile?.delete()
            }
        }
    }

    // ============= STATE MANAGEMENT =============

    fun resetState() {
        _uiState.value = AddMovieUiState()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    // ============= PRIVATE HELPERS =============

    private fun buildFormData(state: AddMovieUiState): MovieFormData {
        val tags = state.tagsInput
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()

        return MovieFormData.builder()
            .title(state.movieTitle.trim())
            .synopsis(state.synopsis.trim().takeIf { it.isNotEmpty() })
            .durationMinutes(state.durationMinutes.toIntOrNull())
            .tags(tags)
            .posterImageUri(state.imageUri)
            .build()
    }

    /**
     * Copia el contenido del URI a un File temporal en caché.
     * Necesario porque Retrofit no puede leer content:// URIs directamente.
     */
    private fun copyUriToTempFile(uriString: String): File {
        val uri = Uri.parse(uriString)
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("No se pudo leer la imagen seleccionada")

        val tempFile = File(context.cacheDir, "movie_upload_${System.currentTimeMillis()}.jpg")
        inputStream.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }

    private fun validateTitle(value: String): String? = when {
        value.isBlank() -> "El título es obligatorio"
        value.length > 255 -> "Máximo 255 caracteres"
        else -> null
    }

    private fun validateDuration(value: String): String? {
        if (value.isEmpty()) return null
        val duration = value.toIntOrNull() ?: return "Duración inválida"
        return when {
            duration <= 0 -> "Debe ser mayor a 0"
            duration > 500 -> "Máximo 500 minutos"
            else -> null
        }
    }
}
