package com.alejandra.amordepelis.features.home.domain.repositories

import com.alejandra.amordepelis.features.home.domain.entities.Announcement
import com.alejandra.amordepelis.features.home.domain.entities.Metrics
import com.alejandra.amordepelis.features.home.domain.entities.Movie

interface HomeRepository {
    suspend fun getMetrics(): Metrics
    suspend fun getRecentMovies(): List<Movie>
    suspend fun getAnnouncements(): List<Announcement>
    suspend fun getLatestNews(): Announcement
}