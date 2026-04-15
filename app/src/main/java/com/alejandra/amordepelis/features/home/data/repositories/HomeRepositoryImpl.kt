package com.alejandra.amordepelis.features.home.data.repositories

import com.alejandra.amordepelis.features.home.data.datasources.remote.api.HomeApi
import com.alejandra.amordepelis.features.home.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.home.data.datasources.remote.mapper.toAnnouncementDomainList
import com.alejandra.amordepelis.features.home.data.datasources.remote.mapper.toHomeMovieList
import com.alejandra.amordepelis.features.home.domain.entities.Announcement
import com.alejandra.amordepelis.features.home.domain.entities.Movie
import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeApi
) : HomeRepository {

    override suspend fun getAllMovies(): List<Movie> {
        return api.getAllMovies().toHomeMovieList()
    }

    override suspend fun getLatestNews(): Announcement {
        return api.getLatestNews().toDomain()
    }

    override suspend fun getAllNews(): List<Announcement> {
        return api.getAllNews().toAnnouncementDomainList()
    }

    override suspend fun createAnnouncement(
        title: String,
        content: String,
        imageFile: File?
    ): Announcement {
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val contentBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = imageFile?.let { file ->
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, reqFile)
        }
        return api.addNews(titleBody, contentBody, imagePart).toDomain()
    }
}
