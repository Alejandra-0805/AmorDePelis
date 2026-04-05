package com.alejandra.amordepelis.features.home.domain.usecases

import javax.inject.Inject

data class HomeUseCases @Inject constructor(
    val getMetrics: GetMetricsUseCase,
    val getRecentMovies: GetRecentMoviesUseCase,
    val getAnnouncements: GetAnnouncementsUseCase
)
