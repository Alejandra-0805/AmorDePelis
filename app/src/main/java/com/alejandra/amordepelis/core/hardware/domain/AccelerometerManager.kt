package com.alejandra.amordepelis.core.hardware.domain

interface AccelerometerManager {
    fun startListening(onShake: () -> Unit)
    fun stopListening()
    fun isListening(): Boolean
}
