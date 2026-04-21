package com.alejandra.amordepelis.core.hardware.domain.usecases

import com.alejandra.amordepelis.core.hardware.domain.CameraManager
import javax.inject.Inject

/**
 * Verifica si el permiso de cámara está concedido.
 */
class CheckCameraPermissionUseCase @Inject constructor(
    private val cameraManager: CameraManager
) {
    operator fun invoke(): Boolean = cameraManager.hasCameraPermission()
}
