package com.alejandra.amordepelis.features.user.di

import com.alejandra.amordepelis.core.di.NetworkModule
import com.alejandra.amordepelis.features.user.data.datasources.remote.api.UserApi

object UserNetworkModule {
    fun provideUserApi(): UserApi {
        return NetworkModule.provideRetrofit(
            gson = NetworkModule.provideGson(),
            baseUrl = NetworkModule.provideBaseUrl()
        ).create(UserApi::class.java)
    }
}
