package com.alejandra.amordepelis.features.movies.domain.usecases

data class MoviesUseCases(
    val getMovies: GetMoviesUseCase,
    val getMovieDetails: GetMovieDetailsUseCase,
    val addMovie: AddMovieUseCase
)
