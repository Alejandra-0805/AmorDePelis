package com.alejandra.amordepelis.features.home.data.datasources.remote.api

import com.alejandra.amordepelis.features.home.data.datasources.remote.model.AnnouncementDto
import com.alejandra.amordepelis.features.home.data.datasources.remote.model.MetricsDto
import com.alejandra.amordepelis.features.home.data.datasources.remote.model.MovieDto
import retrofit2.http.GET

interface HomeApi {
    @GET("metrics")
    suspend fun getMetrics(): MetricsDto

    @GET("movies/recent")
    suspend fun getRecentMovies(): List<MovieDto>

    @GET("announcements")
    suspend fun getAnnouncements(): List<AnnouncementDto>
}