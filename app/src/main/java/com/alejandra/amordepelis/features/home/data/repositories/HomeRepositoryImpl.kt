package com.alejandra.amordepelis.features.home.data.repositories

import com.alejandra.amordepelis.features.home.data.datasources.remote.api.HomeApi
import com.alejandra.amordepelis.features.home.data.datasources.remote.mapper.toAnnouncementDomainList
import com.alejandra.amordepelis.features.home.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.home.data.datasources.remote.mapper.toDomainList
import com.alejandra.amordepelis.features.home.domain.entities.Announcement
import com.alejandra.amordepelis.features.home.domain.entities.Metrics
import com.alejandra.amordepelis.features.home.domain.entities.Movie
import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi
) : HomeRepository {
    
    override suspend fun getMetrics(): Metrics {
        return api.getMetrics().toDomain()
    }

    override suspend fun getRecentMovies(): List<Movie> {
        return api.getRecentMovies().toDomainList()
    }

    override suspend fun getAnnouncements(): List<Announcement> {
        return api.getAnnouncements().toAnnouncementDomainList()
    }

    override suspend fun getLatestNews(): Announcement {
        return api.getLatestNews().toDomain()
    }
}