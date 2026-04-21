package com.alejandra.amordepelis.features.movies.data.datasources.remote.api

import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MoviesApi {
    @GET("movies")
    suspend fun getMovies(): List<MovieDto>

    @GET("movies/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDto

    @GET("movies")
    suspend fun searchMovies(@Query("title") title: String): List<MovieDto>

    @Multipart
    @POST("movies")
    suspend fun addMovie(
        @Part("title") title: RequestBody,
        @Part("synopsis") synopsis: RequestBody?,
        @Part("durationMinutes") duration: RequestBody?,
        @Part("tags") tags: RequestBody?,
        @Part image: MultipartBody.Part?
    ): MovieDto
}
