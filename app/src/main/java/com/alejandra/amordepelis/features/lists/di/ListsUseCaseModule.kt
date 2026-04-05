package com.alejandra.amordepelis.features.lists.di

import com.alejandra.amordepelis.features.lists.domain.repositories.ListsRepository
import com.alejandra.amordepelis.features.lists.domain.usecases.CreateSharedListUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.DeleteSharedListUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.GetAnnouncementsUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.GetSharedListDetailsUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.GetSharedListsUseCase
import com.alejandra.amordepelis.features.lists.domain.usecases.ListsUseCases
import com.alejandra.amordepelis.features.lists.domain.usecases.UpdateSharedListUseCase
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
    fun provideUpdateSharedListUseCase(repository: ListsRepository): UpdateSharedListUseCase {
        return UpdateSharedListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteSharedListUseCase(repository: ListsRepository): DeleteSharedListUseCase {
        return DeleteSharedListUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAnnouncementsUseCase(repository: ListsRepository): GetAnnouncementsUseCase {
        return GetAnnouncementsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideListsUseCases(
        getSharedListsUseCase: GetSharedListsUseCase,
        getSharedListDetailsUseCase: GetSharedListDetailsUseCase,
        createSharedListUseCase: CreateSharedListUseCase,
        updateSharedListUseCase: UpdateSharedListUseCase,
        deleteSharedListUseCase: DeleteSharedListUseCase,
        getAnnouncementsUseCase: GetAnnouncementsUseCase
    ): ListsUseCases {
        return ListsUseCases(
            getSharedLists = getSharedListsUseCase,
            getSharedListDetails = getSharedListDetailsUseCase,
            createSharedList = createSharedListUseCase,
            updateSharedList = updateSharedListUseCase,
            deleteSharedList = deleteSharedListUseCase,
            getAnnouncements = getAnnouncementsUseCase
        )
    }
}
