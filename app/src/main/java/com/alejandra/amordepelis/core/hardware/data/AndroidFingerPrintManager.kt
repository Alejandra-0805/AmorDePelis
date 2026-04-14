package com.alejandra.amordepelis.core.hardware.data

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.alejandra.amordepelis.core.hardware.domain.FingerPrintManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidFingerPrintManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) : FingerPrintManager {

    private val biometricManager: BiometricManager = BiometricManager.from(context)

    override fun hasFingerPrint(): Boolean {
        val result = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        Log.d("BiometricFlow", "hasFingerPrint check: result=$result, hasHardware=${result != BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE}")
        return result != BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
    }

    override fun hasEnrolledFingerPrints(): Boolean {
        val result = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        Log.d("BiometricFlow", "hasEnrolledFingerPrints check: result=$result, isSuccess=${result == BiometricManager.BIOMETRIC_SUCCESS}")
        return result == BiometricManager.BIOMETRIC_SUCCESS
    }

    override fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (errorCode: Int, errorMessage: String) -> Unit,
        onFailed: () -> Unit
    ) {
        Log.d("BiometricFlow", "authenticate() called with activity: ${activity.javaClass.name}")
        val executor = ContextCompat.getMainExecutor(context)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                Log.d("BiometricFlow", "onAuthenticationSucceeded")
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                Log.e("BiometricFlow", "onAuthenticationError: code=$errorCode, msg=$errString")
                onError(errorCode, errString.toString())
            }

            override fun onAuthenticationFailed() {
                Log.w("BiometricFlow", "onAuthenticationFailed")
                onFailed()
            }
        }

        val biometricPrompt = BiometricPrompt(activity, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación biométrica")
            .setSubtitle("Usa tu huella dactilar para continuar")
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()

        Log.d("BiometricFlow", "Calling biometricPrompt.authenticate()")
        biometricPrompt.authenticate(promptInfo)
    }
}