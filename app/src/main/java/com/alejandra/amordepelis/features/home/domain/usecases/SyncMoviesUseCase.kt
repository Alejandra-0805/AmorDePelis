package com.alejandra.amordepelis.features.home.domain.usecases

import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class SyncMoviesUseCase @Inject constructor(
    val repository: HomeRepository
) {
    suspend operator fun invoke() {
        return repository.syncMovies()
    }
}