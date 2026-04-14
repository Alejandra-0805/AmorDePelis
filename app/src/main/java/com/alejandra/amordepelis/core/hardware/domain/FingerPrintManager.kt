package com.alejandra.amordepelis.core.hardware.domain

import androidx.fragment.app.FragmentActivity

interface FingerPrintManager {
    fun hasFingerPrint(): Boolean
    fun hasEnrolledFingerPrints(): Boolean
    fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (errorCode: Int, errorMessage: String) -> Unit,
        onFailed: () -> Unit
    )
}