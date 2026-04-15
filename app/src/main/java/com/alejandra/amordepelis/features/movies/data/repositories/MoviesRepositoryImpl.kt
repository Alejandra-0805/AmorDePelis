package com.alejandra.amordepelis.features.movies.data.repositories

import com.alejandra.amordepelis.features.movies.data.datasources.remote.api.MoviesApi
import com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi
) : MoviesRepository {
    override suspend fun getMovies(): List<Movie> {
        return moviesApi.getMovies().map { it.toDomain() }
    }

    override suspend fun searchMovies(title: String): List<Movie> {
        return moviesApi.searchMovies(title).map { it.toDomain() }
    }

    override suspend fun addMovie(
        title: String,
        synopsis: String?,
        durationMinutes: Int?,
        tags: String?,
        imageFile: java.io.File?
    ): Movie {
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val synopsisBody = synopsis?.toRequestBody("text/plain".toMediaTypeOrNull())
        val durationBody = durationMinutes?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        val tagsBody = tags?.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = imageFile?.let { file ->
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, reqFile)
        }

        return moviesApi.addMovie(
            title = titleBody,
            synopsis = synopsisBody,
            duration = durationBody,
            tags = tagsBody,
            image = imagePart
        ).toDomain()
    }
}
