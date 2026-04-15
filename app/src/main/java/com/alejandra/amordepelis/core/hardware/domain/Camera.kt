package com.alejandra.amordepelis.core.hardware.domain

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

interface Camera {
    fun hasCamera(): Boolean
    fun startPreview(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onCameraReady: () -> Unit = {}
    )
    fun takePhoto(
        onPhotoCaptured: (Uri) -> Unit,
        onError: (Exception) -> Unit
    )
    fun stopCamera()
}
