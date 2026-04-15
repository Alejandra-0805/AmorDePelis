package com.alejandra.amordepelis.features.home.domain.usecases

import javax.inject.Inject

data class HomeUseCases @Inject constructor(
    val getAllMovies: GetAllMoviesUseCase,
    val getAllNews: GetAllNewsUseCase,
    val getLatestNews: GetLatestNewsUseCase,
    val createAnnouncement: CreateAnnouncementUseCase
)
