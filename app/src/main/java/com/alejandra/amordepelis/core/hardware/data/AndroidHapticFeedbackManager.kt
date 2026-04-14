package com.alejandra.amordepelis.core.hardware.data

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.util.Log
import com.alejandra.amordepelis.core.hardware.domain.AppVibrator
import com.alejandra.amordepelis.core.hardware.domain.HapticFeedbackManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidHapticFeedbackManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val vibrator: AppVibrator
) : HapticFeedbackManager {

    companion object {
        private const val NOTIFICATION_VIBRATION_MS = 200L
        private const val TAG = "HapticManager"
    }

    override fun vibrateForNotification() {
        if (!vibrator.hasVibrator()) {
            Log.w(TAG, "Vibración cancelada: El dispositivo no tiene hardware de vibración.")
            return
        }

        if (isDoNotDisturbActive()) {
            Log.w(TAG, "Vibración cancelada: El teléfono está en modo 'No Molestar'.")
            return
        }

        if (isSilentModeActive()) {
            Log.w(TAG, "Vibración cancelada: El teléfono está en modo 'Silencio Total'.")
            return
        }

        Log.d(TAG, "¡Todo en orden! Mandando a vibrar por $NOTIFICATION_VIBRATION_MS ms")
        vibrator.vibrate(NOTIFICATION_VIBRATION_MS)
    }

    private fun isDoNotDisturbActive(): Boolean {
        return try {
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                ?: return false

            val filter = nm.currentInterruptionFilter
            filter != NotificationManager.INTERRUPTION_FILTER_ALL &&
                    filter != NotificationManager.INTERRUPTION_FILTER_UNKNOWN
        } catch (e: Exception) {
            Log.e(TAG, "Error leyendo el estado DND: ${e.message}")
            false
        }
    }
    private fun isSilentModeActive(): Boolean {
        return try {
            val am = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                ?: return false
            // RINGER_MODE_SILENT bloquea vibraciones. RINGER_MODE_VIBRATE sí permite vibrar.
            am.ringerMode == AudioManager.RINGER_MODE_SILENT
        } catch (e: Exception) {
            false
        }
    }
}