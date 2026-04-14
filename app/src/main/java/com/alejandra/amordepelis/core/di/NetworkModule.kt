package com.alejandra.amordepelis.core.di

import com.alejandra.amordepelis.core.network.inteceptor.AuthInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val DEFAULT_BASE_URL = "http://52.7.191.169/api/v1/"

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Named("baseUrl")
    fun provideBaseUrl(): String = DEFAULT_BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @AmorDePelisApi
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient,
        @Named("baseUrl") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}