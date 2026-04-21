package com.alejandra.amordepelis.core.network.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Maneja la detección de conectividad de red
 * Permite que el repositorio decida si debe sincronizar o no
 */
@Singleton
class ConnectivityManager @Inject constructor(
    private val context: Context
) {

    private val connectivityManager = 
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnected = MutableStateFlow(isNetworkAvailable())
    val isConnected: Flow<Boolean> = _isConnected.asStateFlow()

    /**
     * Verifica si hay conectividad de red disponible
     * Soporta Android 6.0 y superiores con NetworkCapabilities
     */
    fun isNetworkAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.isConnectedOrConnecting ?: false
        }
    }

    /**
     * Detecta cambios en la conectividad
     * Llamar cuando se inicializa o cuando cambia el estado
     */
    fun updateConnectivityState() {
        _isConnected.value = isNetworkAvailable()
    }
}
