package com.alejandra.amordepelis.features.home.di

import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import com.alejandra.amordepelis.features.home.domain.usecases.CreateAnnouncementUseCase
import com.alejandra.amordepelis.features.home.domain.usecases.GetAllMoviesUseCase
import com.alejandra.amordepelis.features.home.domain.usecases.GetAllNewsUseCase
import com.alejandra.amordepelis.features.home.domain.usecases.GetLatestNewsUseCase
import com.alejandra.amordepelis.features.home.domain.usecases.HomeUseCases
import com.alejandra.amordepelis.features.home.domain.usecases.SyncMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeUseCaseModule {

    @Provides
    @Singleton
    fun provideGetAllMoviesUseCase(repository: HomeRepository): GetAllMoviesUseCase {
        return GetAllMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllNewsUseCase(repository: HomeRepository): GetAllNewsUseCase {
        return GetAllNewsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetLatestNewsUseCase(repository: HomeRepository): GetLatestNewsUseCase {
        return GetLatestNewsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateAnnouncementUseCase(repository: HomeRepository): CreateAnnouncementUseCase {
        return CreateAnnouncementUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSyncMoviesUseCase(repository: HomeRepository): SyncMoviesUseCase {
        return SyncMoviesUseCase(repository)
    }


    @Provides
    @Singleton
    fun provideHomeUseCases(
        getAllMoviesUseCase: GetAllMoviesUseCase,
        getAllNewsUseCase: GetAllNewsUseCase,
        getLatestNewsUseCase: GetLatestNewsUseCase,
        createAnnouncementUseCase: CreateAnnouncementUseCase,
        syncMoviesUseCase: SyncMoviesUseCase
    ): HomeUseCases {
        return HomeUseCases(
            getAllMovies = getAllMoviesUseCase,
            getAllNews = getAllNewsUseCase,
            getLatestNews = getLatestNewsUseCase,
            createAnnouncement = createAnnouncementUseCase,
            syncMovies = syncMoviesUseCase
        )
    }
}
