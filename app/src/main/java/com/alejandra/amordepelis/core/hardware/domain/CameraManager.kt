package com.alejandra.amordepelis.core.hardware.domain

import android.net.Uri

interface CameraManager {
    /**
     * Prepara un URI temporal para la foto de cámara y lo devuelve.
     * El llamador (Screen) es responsable de lanzar el ActivityResultLauncher con este URI.
     */
    fun createTempCameraUri(): Uri?

    /**
     * Verifica si el permiso de cámara está concedido.
     */
    fun hasCameraPermission(): Boolean
}
