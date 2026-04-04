package com.alejandra.amordepelis.features.lists.di

import com.alejandra.amordepelis.features.lists.domain.usecases.CreateSharedListUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.DeleteSharedListUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.GetSharedListDetailsUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.GetSharedListsUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.ListsUseCases
import com.alejandra.amordepelis.features.lists.domain.usecases.UpdateSharedListUseCase

object ListsUseCaseModule {
    fun provideListsUseCases(): ListsUseCases {
        val repository = ListsRepositoryModule().provideListsRepository()
        return ListsUseCases(
            getSharedLists = GetSharedListsUseCase(repository),
            getSharedListDetails = GetSharedListDetailsUseCase(repository),
            createSharedList = CreateSharedListUseCase(repository),
            updateSharedList = UpdateSharedListUseCase(repository),
            deleteSharedList = DeleteSharedListUseCase(repository)
        )
    }
}
