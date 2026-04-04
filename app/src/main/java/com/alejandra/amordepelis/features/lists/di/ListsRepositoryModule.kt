package com.alejandra.amordepelis.features.lists.di

import com.alejandra.amordepelis.features.lists.data.repositories.ListsRepositoryImpl
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository

class ListsRepositoryModule {
    fun provideListsRepository(): ListsRepository {
        return ListsRepositoryImpl(api = ListsNetworkModule.provideListsApi())
    }
}
