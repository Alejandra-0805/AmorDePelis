package com.alejandra.amordepelis.features.movies.domain.usecases

import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository

class GetMovieDetailsUseCase(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(movieId: String): Movie = repository.getMovieById(movieId)
}
