package com.alejandra.amordepelis.features.home.presentation.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandra.amordepelis.core.hardware.domain.CameraManager
import com.alejandra.amordepelis.core.hardware.domain.HapticFeedbackManager
import com.alejandra.amordepelis.features.home.domain.usecases.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

data class AddAnnouncementUiState(
    val title: String = "",
    val content: String = "",
    val imageUri: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)

@HiltViewModel
class AddAnnouncementViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    @ApplicationContext private val context: Context,
    private val hapticFeedbackManager: HapticFeedbackManager,
    private val cameraManager: CameraManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAnnouncementUiState())
    val uiState: StateFlow<AddAnnouncementUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun updateContent(content: String) {
        _uiState.update { it.copy(content = content) }
    }

    fun updateImageUri(uri: String?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetState() {
        _uiState.value = AddAnnouncementUiState()
    }

    // ── Cámara ───────────────────────────────────────────────────────────────

    /** Crea un URI temporal para la cámara. Devuelve null si FileProvider falla. */
    fun createCameraUri(): Uri? = cameraManager.createTempCameraUri()

    /** Comprueba si el permiso de cámara está concedido. */
    fun hasCameraPermission(): Boolean = cameraManager.hasCameraPermission()

    fun submit() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(error = "El título es obligatorio") }
            return
        }
        if (state.content.isBlank()) {
            _uiState.update { it.copy(error = "El contenido es obligatorio") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            var tempFile: File? = null
            if (state.imageUri != null) {
                try {
                    val uri = Uri.parse(state.imageUri)
                    val inputStream = context.contentResolver.openInputStream(uri)
                    if (inputStream != null) {
                        tempFile = File(context.cacheDir, "upload_news_${System.currentTimeMillis()}.jpg")
                        val outputStream = FileOutputStream(tempFile)
                        inputStream.copyTo(outputStream)
                        inputStream.close()
                        outputStream.close()
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false, error = "Error al procesar la imagen") }
                    return@launch
                }
            }

            runCatching {
                homeUseCases.createAnnouncement(
                    title = state.title,
                    content = state.content,
                    imageFile = tempFile
                )
            }.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
                tempFile?.delete()
                hapticFeedbackManager.vibrateForNotification()
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = throwable.message ?: "No se pudo crear el anuncio"
                    )
                }
                tempFile?.delete()
            }
        }
    }
}
