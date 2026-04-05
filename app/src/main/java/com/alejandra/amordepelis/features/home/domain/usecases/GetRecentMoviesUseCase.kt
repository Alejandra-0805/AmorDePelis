package com.alejandra.amordepelis.features.home.domain.usecases

import com.alejandra.amordepelis.features.home.domain.entities.Movie
import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class GetRecentMoviesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): List<Movie> {
        return repository.getRecentMovies()
    }
}