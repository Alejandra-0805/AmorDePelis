package com.alejandra.amordepelis.features.movies.di

import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import com.alejandra.amordepelis.features.movies.domain.usecases.GetMoviesUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.SearchMoviesUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.AddMovieUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.MoviesUseCases
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
        searchMoviesUseCase: SearchMoviesUseCase,
        addMovieUseCase: AddMovieUseCase
    ): MoviesUseCases {
        return MoviesUseCases(
            getMovies = getMoviesUseCase,
            searchMovies = searchMoviesUseCase,
            addMovie = addMovieUseCase
        )
    }
}
