package com.alejandra.amordepelis.features.home.data.repositories

import android.util.Log
import com.alejandra.amordepelis.core.network.connectivity.ConnectivityManager
import com.alejandra.amordepelis.features.home.data.datasources.local.LocalMovieDataSource
import com.alejandra.amordepelis.features.home.data.datasources.local.mapper.toEntity
import com.alejandra.amordepelis.features.home.data.datasources.remote.RemoteMovieDataSource
import com.alejandra.amordepelis.features.home.data.datasources.remote.api.HomeApi
import com.alejandra.amordepelis.features.home.data.datasources.remote.mapper.toDomain
import com.alejandra.amordepelis.features.home.data.datasources.remote.mapper.toAnnouncementDomainList
import com.alejandra.amordepelis.features.home.domain.entities.Announcement
import com.alejandra.amordepelis.features.home.domain.entities.Movie
import com.alejandra.amordepelis.features.home.domain.repositories.HomeRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepositoryImpl @Inject constructor(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val api: HomeApi,
    private val connectivityManager: ConnectivityManager
) : HomeRepository {

    companion object {
        private const val TAG = "HomeRepositoryImpl"
    }

    override suspend fun getAllMovies(): List<Movie> = withContext(Dispatchers.IO) {
        try {
            val localMovies = localMovieDataSource.getAllMovies()
            
            if (connectivityManager.isNetworkAvailable()) {
                launchBackgroundSync()
            } else {
                Log.d(TAG, "Sin conectividad: usando datos locales cacheados")
            }
            
            localMovies
        } catch (e: Exception) {
            Log.e(TAG, "Error al obtener películas locales", e)
            try {
                remoteMovieDataSource.getAllMoviesAsDomain()
            } catch (apiException: Exception) {
                Log.e(TAG, "Error al obtener películas del servidor", apiException)
                emptyList()
            }
        }
    }

    override suspend fun syncMovies() {
        withContext(Dispatchers.IO) {
            try {
                val remoteMovies = remoteMovieDataSource.getAllMovies()
                val movieEntities = remoteMovies.map { it.toEntity() }
                
                localMovieDataSource.replaceAllMovies(movieEntities)
                
                Log.d(TAG, "Películas sincronizadas exitosamente. Total: ${movieEntities.size}")
            } catch (e: Exception) {
                Log.e(TAG, "Error sincronizando películas", e)
                throw e
            }
        }
    }

    private suspend fun launchBackgroundSync() {
        try {
            syncMovies()
            Log.d(TAG, "Sincronización en background completada exitosamente")
        } catch (e: Exception) {
            Log.w(TAG, "Sincronización en background falló - usando datos locales", e)
        }
    }

    override suspend fun getLatestNews(): Announcement = withContext(Dispatchers.IO) {
        return@withContext api.getLatestNews().toDomain()
    }

    override suspend fun getAllNews(): List<Announcement> = withContext(Dispatchers.IO) {
        return@withContext api.getAllNews().toAnnouncementDomainList()
    }

    override suspend fun createAnnouncement(
        title: String,
        content: String,
        imageFile: File?
    ): Announcement = withContext(Dispatchers.IO) {
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val contentBody = content.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = imageFile?.let { file ->
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, reqFile)
        }
        return@withContext api.addNews(titleBody, contentBody, imagePart).toDomain()
    }
}

