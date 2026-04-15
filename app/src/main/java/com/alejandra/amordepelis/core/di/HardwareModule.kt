package com.alejandra.amordepelis.core.di

import com.alejandra.amordepelis.core.hardware.data.AndroidAppVibrator
import com.alejandra.amordepelis.core.hardware.data.AndroidHapticFeedbackManager
import com.alejandra.amordepelis.core.hardware.domain.AppVibrator
import com.alejandra.amordepelis.core.hardware.domain.HapticFeedbackManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Binds
    abstract fun bindAppVibrator(
        appVibratorImpl: AndroidAppVibrator
    ): AppVibrator

    @Binds
    abstract fun bindHapticFeedbackManager(
        hapticFeedbackManagerImpl: AndroidHapticFeedbackManager
    ): HapticFeedbackManager
}
