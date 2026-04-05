package com.alejandra.amordepelis.features.movies.data.datasources.remote.api

import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.AddMovieRequestDto
import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MoviesApi {
    @GET("movies")
    suspend fun getMovies(): List<MovieDto>

    @GET("movies/{movieId}")
    suspend fun getMovieById(@Path("movieId") movieId: String): MovieDto

    @POST("movies")
    suspend fun addMovie(@Body request: AddMovieRequestDto): MovieDto
}
