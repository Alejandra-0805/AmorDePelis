package com.alejandra.amordepelis.features.lists.di

import com.alejandra.amordepelis.features.lists.data.datasources.remote.api.ListsApi

object ListsNetworkModule {
    fun provideListsApi(): ListsApi {
        throw NotImplementedError("Provide ListsApi implementation")
    }
}
