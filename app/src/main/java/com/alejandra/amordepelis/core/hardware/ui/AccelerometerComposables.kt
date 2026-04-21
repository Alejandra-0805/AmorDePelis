package com.alejandra.amordepelis.core.hardware.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.alejandra.amordepelis.core.hardware.domain.AccelerometerManager

/**
 * Composable que activa el acelerómetro mientras está en composición y lo detiene al salir.
 *
 * Uso:
 * ```
 * val accelerometerManager: AccelerometerManager = hiltViewModel<SomeVM>().accelerometerManager
 * rememberShakeDetector(
 *     manager = accelerometerManager,
 *     onShake = { /* reaccionar al shake */ }
 * )
 * ```
 */
@Composable
fun rememberShakeDetector(
    manager: AccelerometerManager,
    onShake: () -> Unit
) {
    DisposableEffect(manager) {
        manager.startListening(onShake)
        onDispose {
            manager.stopListening()
        }
    }
}
