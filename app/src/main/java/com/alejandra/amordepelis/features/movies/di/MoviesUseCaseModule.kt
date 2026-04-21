package com.alejandra.amordepelis.features.movies.di

import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import com.alejandra.amordepelis.features.movies.domain.usecases.GetMoviesUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.GetMovieDetailsUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.SearchMoviesUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.AddMovieUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.MoviesUseCases
import com.alejandra.amordepelis.features.movies.domain.usecases.SyncMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoviesUseCaseModule {

    @Provides
    @Singleton
    fun provideGetMoviesUseCase(repository: MoviesRepository): GetMoviesUseCase {
        return GetMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetMovieDetailsUseCase(repository: MoviesRepository): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSearchMoviesUseCase(repository: MoviesRepository): SearchMoviesUseCase {
        return SearchMoviesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddMovieUseCase(repository: MoviesRepository): AddMovieUseCase {
        return AddMovieUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideMoviesUseCases(
        getMoviesUseCase: GetMoviesUseCase,
        getMovieDetailsUseCase: GetMovieDetailsUseCase,
        searchMoviesUseCase: SearchMoviesUseCase,
        addMovieUseCase: AddMovieUseCase,
        syncMoviesUseCase: SyncMoviesUseCase
    ): MoviesUseCases {
        return MoviesUseCases(
            getMovies = getMoviesUseCase,
            getMovieDetails = getMovieDetailsUseCase,
            searchMovies = searchMoviesUseCase,
            addMovie = addMovieUseCase,
            syncMovies = syncMoviesUseCase
        )
    }
}
