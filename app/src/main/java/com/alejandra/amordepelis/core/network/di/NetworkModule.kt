package com.alejandra.amordepelis.core.network.di

import android.content.Context
import com.alejandra.amordepelis.core.network.connectivity.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de inyección para componentes de red
 * Provee instancias singleton de ConnectivityManager
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager = ConnectivityManager(context)
}
