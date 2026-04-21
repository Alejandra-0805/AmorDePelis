package com.alejandra.amordepelis.core.hardware.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Diálogo reutilizable para seleccionar el origen de una foto (cámara o galería).
 *
 * @param isVisible    Si el diálogo debe mostrarse.
 * @param title        Título del diálogo.
 * @param onDismiss    Acción al cerrar sin selección.
 * @param onCameraClick Acción al elegir "Cámara".
 * @param onGalleryClick Acción al elegir "Galería".
 */
@Composable
fun PhotoSelectionDialog(
    isVisible: Boolean,
    title: String = "Seleccionar foto",
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    if (!isVisible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text("¿Desde dónde quieres obtener la imagen?") },
        confirmButton = {
            TextButton(onClick = onCameraClick) {
                Text("Cámara")
            }
        },
        dismissButton = {
            TextButton(onClick = onGalleryClick) {
                Text("Galería")
            }
        }
    )
}
