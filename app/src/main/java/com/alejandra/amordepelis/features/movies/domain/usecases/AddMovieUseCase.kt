package com.alejandra.amordepelis.features.movies.domain.usecases

import com.alejandra.amordepelis.features.movies.domain.entities.AddMovieParams
import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

class AddMovieUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(params: AddMovieParams): Movie = repository.addMovie(params)
}
