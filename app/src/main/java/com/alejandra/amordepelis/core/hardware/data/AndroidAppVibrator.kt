package com.alejandra.amordepelis.core.hardware.data

import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.alejandra.amordepelis.core.hardware.domain.AppVibrator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidAppVibrator @Inject constructor(
    @ApplicationContext private val context: Context
) : AppVibrator {

    // Soporte para Android 12+ (VibratorManager) y versiones anteriores (Vibrator)
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun hasVibrator(): Boolean {
        return vibrator.hasVibrator()
    }

    override fun vibrate(durationMs: Long) {
        if (!hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0+: Usamos VibrationEffect
            val effect = VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)

            // ¡ESTA ES LA CLAVE! Le decimos al sistema que esto es una Notificación
            // Así ZTE, Samsung o Xiaomi no lo ignoran.
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT)
                .build()

            vibrator.vibrate(effect, audioAttributes)
        } else {
            // Android 7 o inferior (código legado)
            @Suppress("DEPRECATION")
            vibrator.vibrate(durationMs)
        }
    }

    override fun vibratePattern(pattern: LongArray, repeat: Int) {
        if (!hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createWaveform(pattern, repeat)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_COMMUNICATION_INSTANT)
                .build()

            vibrator.vibrate(effect, audioAttributes)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, repeat)
        }
    }

    override fun cancel() {
        vibrator.cancel()
    }
}