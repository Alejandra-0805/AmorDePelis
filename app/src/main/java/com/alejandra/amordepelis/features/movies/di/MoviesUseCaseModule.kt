package com.alejandra.amordepelis.features.movies.di

import com.alejandra.amordepelis.features.movies.domain.usecases.AddMovieUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.GetMovieDetailsUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.GetMoviesUseCase
import com.alejandra.amordepelis.features.movies.domain.usecases.MoviesUseCases

object MoviesUseCaseModule {
    fun provideMoviesUseCases(): MoviesUseCases {
        val repository = MoviesRepositoryModule().provideMoviesRepository()
        return MoviesUseCases(
            getMovies = GetMoviesUseCase(repository),
            getMovieDetails = GetMovieDetailsUseCase(repository),
            addMovie = AddMovieUseCase(repository)
        )
    }
}
