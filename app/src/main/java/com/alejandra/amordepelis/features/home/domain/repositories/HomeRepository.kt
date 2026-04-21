package com.alejandra.amordepelis.features.home.domain.repositories

import com.alejandra.amordepelis.features.home.domain.entities.Announcement
import com.alejandra.amordepelis.features.home.domain.entities.Movie
import java.io.File

interface HomeRepository {
    suspend fun getAllMovies(): List<Movie>
    suspend fun getLatestNews(): Announcement
    suspend fun getAllNews(): List<Announcement>
    suspend fun createAnnouncement(title: String, content: String, imageFile: File?): Announcement
    suspend fun syncMovies()
}