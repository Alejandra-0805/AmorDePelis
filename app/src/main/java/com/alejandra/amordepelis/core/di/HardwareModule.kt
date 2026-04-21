package com.alejandra.amordepelis.core.di

import com.alejandra.amordepelis.core.hardware.data.AndroidAccelerometerManager
import com.alejandra.amordepelis.core.hardware.data.AndroidAppVibrator
import com.alejandra.amordepelis.core.hardware.data.AndroidCameraManager
import com.alejandra.amordepelis.core.hardware.data.AndroidHapticFeedbackManager
import com.alejandra.amordepelis.core.hardware.domain.AccelerometerManager
import com.alejandra.amordepelis.core.hardware.domain.AppVibrator
import com.alejandra.amordepelis.core.hardware.domain.CameraManager
import com.alejandra.amordepelis.core.hardware.domain.HapticFeedbackManager
import com.alejandra.amordepelis.core.hardware.domain.usecases.CameraUseCases
import com.alejandra.amordepelis.core.hardware.domain.usecases.CheckCameraPermissionUseCase
import com.alejandra.amordepelis.core.hardware.domain.usecases.CreateCameraUriUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    // ── Vibración ─────────────────────────────────────────────────────────────

    @Binds
    abstract fun bindAppVibrator(
        appVibratorImpl: AndroidAppVibrator
    ): AppVibrator

    @Binds
    abstract fun bindHapticFeedbackManager(
        hapticFeedbackManagerImpl: AndroidHapticFeedbackManager
    ): HapticFeedbackManager

    // ── Acelerómetro ───────────────────────────────────────────────────────────

    @Binds
    @Singleton
    abstract fun bindAccelerometerManager(
        impl: AndroidAccelerometerManager
    ): AccelerometerManager

    // ── Cámara ─────────────────────────────────────────────────────────────────

    @Binds
    @Singleton
    abstract fun bindCameraManager(
        impl: AndroidCameraManager
    ): CameraManager

    companion object {
        @Provides
        @Singleton
        fun provideCameraUseCases(cameraManager: CameraManager): CameraUseCases =
            CameraUseCases(
                createCameraUri = CreateCameraUriUseCase(cameraManager),
                checkCameraPermission = CheckCameraPermissionUseCase(cameraManager)
            )
    }
}
