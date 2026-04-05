package com.alejandra.amordepelis.features.lists.di

import com.alejandra.amordepelis.features.lists.data.repositories.ListsRepositoryImpl
import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ListsRepositoryModule {
    @Binds
    abstract fun bindListsRepository(
        listsRepositoryImpl: ListsRepositoryImpl
    ): ListsRepository
}
