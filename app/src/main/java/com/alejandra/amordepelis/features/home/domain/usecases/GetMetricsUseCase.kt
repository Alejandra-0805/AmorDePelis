package com.alejandra.amordepelis.features.home.domain.usecases

import com.alejandra.amordepelis.features.home.domain.entities.Metrics
import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class GetMetricsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): Metrics {
        return repository.getMetrics()
    }
}