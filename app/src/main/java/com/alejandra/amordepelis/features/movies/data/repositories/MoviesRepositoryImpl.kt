package com.alejandra.amordepelis.features.movies.data.repositories

import android.util.Log
import com.alejandra.amordepelis.core.network.connectivity.ConnectivityManager
import com.alejandra.amordepelis.core.database.entities.MovieEntity
import com.alejandra.amordepelis.features.movies.data.datasources.local.LocalMovieDataSource
import com.alejandra.amordepelis.features.movies.data.datasources.local.mapper.toEntity
import com.alejandra.amordepelis.features.movies.data.datasources.remote.RemoteMovieDataSource
import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val connectivityManager: ConnectivityManager
) : MoviesRepository {

    companion object {
        private const val TAG = "MoviesRepositoryImpl"
    }

    override fun getMoviesStream(): Flow<List<Movie>> =
        localMovieDataSource.observeMovies()

    override suspend fun syncMovies(): Unit = withContext(Dispatchers.IO) {
        Log.d(TAG, "Iniciando sincronización de películas...")
        val remoteMovies = remoteMovieDataSource.getAllMovies()
        val movieEntities = remoteMovies.map { it.toEntity() }
        localMovieDataSource.replaceAllMovies(movieEntities)
        Log.d(TAG, "Sincronización completada. Total: ${movieEntities.size} películas.")
    }

    override suspend fun getMovieDetails(id: Int): Movie = withContext(Dispatchers.IO) {
        if (connectivityManager.isNetworkAvailable()) {
            try {
                val movie = remoteMovieDataSource.getMovieDetails(id)
                Log.d(TAG, "Detalles obtenidos del servidor: $id")
                movie
            } catch (e: Exception) {
                Log.w(TAG, "Error obteniendo detalles del servidor, usando cache local", e)
                localMovieDataSource.getMovieById(id)
                    ?: throw Exception("Película no encontrada ni en servidor ni en caché local.")
            }
        } else {
            Log.d(TAG, "Sin conectividad: usando cache local para detalles de $id")
            localMovieDataSource.getMovieById(id)
                ?: throw Exception("Película $id no disponible en caché local.")
        }
    }

    override suspend fun searchMovies(title: String): List<Movie> = withContext(Dispatchers.IO) {
        if (connectivityManager.isNetworkAvailable()) {
            try {
                remoteMovieDataSource.searchMovies(title)
            } catch (e: Exception) {
                Log.w(TAG, "Error en búsqueda remota, filtrando cache local", e)
                filterLocalMovies(title)
            }
        } else {
            Log.d(TAG, "Sin conectividad: búsqueda offline en cache local")
            filterLocalMovies(title)
        }
    }


    override suspend fun addMovie(
        title: String,
        synopsis: String?,
        durationMinutes: Int?,
        tags: String?,
        imageFile: File?
    ): Movie = withContext(Dispatchers.IO) {
        val movie = remoteMovieDataSource.addMovie(
            title = title,
            synopsis = synopsis,
            durationMinutes = durationMinutes,
            tags = tags,
            imageFile = imageFile
        )

        localMovieDataSource.saveMovie(
            MovieEntity(
                id = movie.id.toInt(),
                titulo = movie.title,
                sinopsis = movie.synopsis ?: "",
                genre = movie.tags.joinToString(", "),
                rating = movie.averageRating?.toInt() ?: 0,
                duracion = movie.durationMinutes ?: 0,
                imageUrl = movie.imageUrl ?: ""
            )
        )

        Log.d(TAG, "Película añadida y cacheada: ${movie.title}")
        movie
    }
    /**
     * Alias de addMovie requerido por CreateMovieWithImageUseCase.
     * Delega a la misma pipeline remote + cache local.
     */
    override suspend fun addMovieWithImage(
        title: String,
        synopsis: String?,
        durationMinutes: Int?,
        tags: String?,
        imageFile: File?
    ): Movie = addMovie(title, synopsis, durationMinutes, tags, imageFile)

    private suspend fun filterLocalMovies(query: String): List<Movie> =
        localMovieDataSource.getAllMovies().filter { movie ->
            movie.title.contains(query, ignoreCase = true) ||
                movie.synopsis?.contains(query, ignoreCase = true) == true
        }
}
