package com.alejandra.amordepelis.core.hardware.domain

interface AppVibrator {
    fun vibrate(durationMs: Long = 200L)
    fun vibratePattern(pattern: LongArray, repeat: Int = -1)
    fun cancel()
    fun hasVibrator(): Boolean
}
