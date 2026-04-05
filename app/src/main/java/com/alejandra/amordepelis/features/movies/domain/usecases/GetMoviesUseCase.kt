package com.alejandra.amordepelis.features.movies.domain.usecases

import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(): List<Movie> = repository.getMovies()
}
