package com.alejandra.amordepelis.core.hardware.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.alejandra.amordepelis.core.hardware.domain.AccelerometerManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class AndroidAccelerometerManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AccelerometerManager, SensorEventListener {

    companion object {
        private const val SHAKE_THRESHOLD_GRAVITY = 2.7f
        private const val SHAKE_SLOP_TIME_MS = 500L
    }

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var onShakeCallback: (() -> Unit)? = null
    private var lastShakeTime: Long = 0
    private var _isListening = false

    override fun startListening(onShake: () -> Unit) {
        if (_isListening) return
        onShakeCallback = onShake
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            _isListening = true
        }
    }

    override fun stopListening() {
        if (!_isListening) return
        sensorManager.unregisterListener(this)
        onShakeCallback = null
        _isListening = false
    }

    override fun isListening(): Boolean = _isListening

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0] / SensorManager.GRAVITY_EARTH
        val y = event.values[1] / SensorManager.GRAVITY_EARTH
        val z = event.values[2] / SensorManager.GRAVITY_EARTH

        val gForce = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            val now = System.currentTimeMillis()
            if (lastShakeTime + SHAKE_SLOP_TIME_MS > now) return
            lastShakeTime = now
            onShakeCallback?.invoke()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No necesario
    }
}
