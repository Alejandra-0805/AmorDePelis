package com.alejandra.amordepelis.features.lists.di

import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import com.alejandra.amordepelis.features.lists.domain.usecases.CreateSharedListUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.GetSharedListDetailsUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.GetSharedListsUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.AddMovieToListUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.ListsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ListsUseCaseModule {

    @Provides
    @Singleton
    fun provideGetSharedListsUseCase(repository: ListsRepository): GetSharedListsUseCase {
        return GetSharedListsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetSharedListDetailsUseCase(repository: ListsRepository): GetSharedListDetailsUseCase {
        return GetSharedListDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateSharedListUseCase(repository: ListsRepository): CreateSharedListUseCase {
        return CreateSharedListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddMovieToListUseCase(repository: ListsRepository): AddMovieToListUseCase {
        return AddMovieToListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideListsUseCases(
        getSharedListsUseCase: GetSharedListsUseCase,
        getSharedListDetailsUseCase: GetSharedListDetailsUseCase,
        createSharedListUseCase: CreateSharedListUseCase,
        addMovieToListUseCase: AddMovieToListUseCase
    ): ListsUseCases {
        return ListsUseCases(
            getSharedLists = getSharedListsUseCase,
            getSharedListDetails = getSharedListDetailsUseCase,
            createSharedList = createSharedListUseCase,
            addMovieToList = addMovieToListUseCase
        )
    }
}
