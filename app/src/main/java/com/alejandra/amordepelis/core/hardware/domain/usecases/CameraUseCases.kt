package com.alejandra.amordepelis.core.hardware.domain.usecases

import com.alejandra.amordepelis.core.hardware.domain.CameraManager
import javax.inject.Inject

/**
 * Agrupa los use cases de cámara para inyección conveniente.
 */
data class CameraUseCases(
    val createCameraUri: CreateCameraUriUseCase,
    val checkCameraPermission: CheckCameraPermissionUseCase
)
