package com.alejandra.amordepelis.features.user.di

import com.alejandra.amordepelis.features.user.data.datasources.remote.api.UserApi

object UserNetworkModule {
    fun provideUserApi(): UserApi {
        throw NotImplementedError("Provide UserApi implementation")
    }
}
