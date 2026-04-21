package com.alejandra.amordepelis.features.movies.data.datasources.remote

import com.alejandra.amordepelis.features.movies.data.datasources.remote.api.MoviesApi
import com.alejandra.amordepelis.features.movies.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto
import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RemoteMovieDataSource @Inject constructor(
    private val moviesApi: MoviesApi
) {

    suspend fun getAllMovies(): List<MovieDto> {
        return moviesApi.getMovies()
    }

    suspend fun getAllMoviesAsDomain(): List<Movie> {
        return getAllMovies().map { it.toDomain() }
    }

    suspend fun getMovieDetails(id: Int): Movie {
        return moviesApi.getMovieDetails(id).toDomain()
    }

    suspend fun searchMovies(title: String): List<Movie> {
        return moviesApi.searchMovies(title).map { it.toDomain() }
    }

    suspend fun addMovie(
        title: String,
        synopsis: String?,
        durationMinutes: Int?,
        tags: String?,
        imageFile: File?
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
