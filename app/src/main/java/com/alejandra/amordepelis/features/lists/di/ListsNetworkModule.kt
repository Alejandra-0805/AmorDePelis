package com.alejandra.amordepelis.features.lists.di

import com.alejandra.amordepelis.core.di.NetworkModule
import com.alejandra.amordepelis.features.lists.data.datasources.remote.api.ListsApi

object ListsNetworkModule {
    fun provideListsApi(): ListsApi {
        return NetworkModule.provideRetrofit(
            gson = NetworkModule.provideGson(),
            baseUrl = NetworkModule.provideBaseUrl()
        ).create(ListsApi::class.java)
    }
}
