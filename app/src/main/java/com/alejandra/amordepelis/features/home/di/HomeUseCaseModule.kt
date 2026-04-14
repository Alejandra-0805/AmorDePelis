package com.alejandra.amordepelis.features.home.di

import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import com.alejandra.amordepelis.features.home.domain.usecases.GetAnnouncementsUseCase
import com.alejandra.amordepelis.features.home.domain.usecases.GetMetricsUseCase
import com.alejandra.amordepelis.features.home.domain.usecases.GetRecentMoviesUseCase
import com.alejandra.amordepelis.features.home.domain.usecases.HomeUseCases
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
    fun provideGetMetricsUseCase(repository: HomeRepository): GetMetricsUseCase {
        return GetMetricsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetRecentMoviesUseCase(repository: HomeRepository): GetRecentMoviesUseCase {
        return GetRecentMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAnnouncementsUseCase(repository: HomeRepository): GetAnnouncementsUseCase {
        return GetAnnouncementsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideHomeUseCases(
        getMetricsUseCase: GetMetricsUseCase,
        getRecentMoviesUseCase: GetRecentMoviesUseCase,
        getAnnouncementsUseCase: GetAnnouncementsUseCase
    ): HomeUseCases {
        return HomeUseCases(
            getMetrics = getMetricsUseCase,
            getRecentMovies = getRecentMoviesUseCase,
            getAnnouncements = getAnnouncementsUseCase
        )
    }
}