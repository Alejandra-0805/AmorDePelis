package com.alejandra.amordepelis.features.home.domain.usecases

import javax.inject.Inject

class HomeUseCases @Inject constructor(
    val login: GetMetricsUseCase,
    val register: GetRecentMoviesUseCase
)
