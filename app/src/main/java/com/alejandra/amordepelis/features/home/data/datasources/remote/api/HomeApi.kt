package com.alejandra.amordepelis.features.home.data.datasources.remote.api

import com.alejandra.amordepelis.features.home.data.datasources.remote.model.NewsDto
import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface HomeApi {
    @GET("movies")
    suspend fun getAllMovies(): List<MovieDto>

    @GET("news/latest")
    suspend fun getLatestNews(): NewsDto

    @GET("news")
    suspend fun getAllNews(): List<NewsDto>

    @Multipart
    @POST("news")
    suspend fun addNews(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part?
    ): NewsDto
}
