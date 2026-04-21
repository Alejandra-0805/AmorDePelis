package com.alejandra.amordepelis.core.hardware.domain.usecases

import android.net.Uri
import com.alejandra.amordepelis.core.hardware.domain.CameraManager
import javax.inject.Inject

/**
 * Crea un URI temporal para capturar una foto con la cámara.
 * El Composable debe lanzar ActivityResultContracts.TakePicture() con el URI devuelto.
 *
 * @return URI listo para pasarle al launcher, o null si FileProvider falla.
 */
class CreateCameraUriUseCase @Inject constructor(
    private val cameraManager: CameraManager
) {
    operator fun invoke(): Uri? = cameraManager.createTempCameraUri()
}
