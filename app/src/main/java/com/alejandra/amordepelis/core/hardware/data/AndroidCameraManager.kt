package com.alejandra.amordepelis.core.hardware.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.alejandra.amordepelis.core.hardware.domain.CameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidCameraManager @Inject constructor(
    @ApplicationContext private val context: Context
) : CameraManager {

    /**
     * Crea un archivo temporal en caché y devuelve su URI via FileProvider.
     * El Composable/ViewModel es responsable de lanzar TakePicture con este URI.
     */
    override fun createTempCameraUri(): Uri? {
        return try {
            val photoFile = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
        } catch (e: Exception) {
            null
        }
    }

    override fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
}
