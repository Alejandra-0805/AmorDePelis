package com.alejandra.amordepelis.features.movies.domain.repositories

import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de dominio para el repositorio de películas.
 *
 * Principio SOLID - Interface Segregation:
 *   Cada método tiene un propósito único y concreto.
 *
 * Principio SOLID - Dependency Inversion:
 *   Las capas superiores (UseCase, ViewModel) dependen de esta interfaz,
 *   nunca de la implementación concreta [MoviesRepositoryImpl].
 *
 * Estrategia Offline-First:
 *   - [getMoviesStream] es el punto de entrada principal para la lista.
 *     Devuelve un [Flow] respaldado por Room, por lo que emite datos locales
 *     de forma inmediata y se actualiza automáticamente cuando [syncMovies]
 *     escribe nuevos datos en la base de datos local.
 *   - [syncMovies] se llama desde el ViewModel cuando hay conectividad;
 *     no retorna datos, solo persiste en Room y deja que el Flow notifique.
 */
interface MoviesRepository {

    /**
     * Stream reactivo de películas almacenadas localmente.
     *
     * El Flow emite:
     *   1. De forma inmediata los datos cacheados en Room (puede ser lista vacía).
     *   2. Automáticamente una nueva emisión cada vez que [syncMovies] actualiza
     *      la tabla, sin necesidad de re-suscripción.
     *
     * @return [Flow] continuo de la lista de películas.
     */
    fun getMoviesStream(): Flow<List<Movie>>

    /**
     * Obtiene los detalles de una película.
     * Intenta primero desde el servidor; cae al cache local si no hay red.
     *
     * @param id ID de la película.
     * @return [Movie] con todos sus detalles.
     */
    suspend fun getMovieDetails(id: Int): Movie

    /**
     * Busca películas por título.
     * Si hay red, consulta el servidor; de lo contrario filtra el cache local.
     *
     * @param title Término de búsqueda.
     * @return Lista de películas coincidentes.
     */
    suspend fun searchMovies(title: String): List<Movie>

    /**
     * Añade una nueva película al catálogo remoto y la cachea localmente.
     * Requiere conectividad; lanza excepción si no hay red.
     */
    suspend fun addMovie(
        title: String,
        synopsis: String?,
        durationMinutes: Int?,
        tags: String?,
        imageFile: java.io.File?
    ): Movie

    /**
     * Sincroniza las películas del servidor hacia Room.
     *
     * Flujo interno:
     *   1. Obtiene la lista completa desde la API remota.
     *   2. Reemplaza todos los datos locales (clear → insertAll).
     *   3. Room notifica el cambio: [getMoviesStream] emite automáticamente.
     *
     * Lanza excepción si la sincronización falla; el ViewModel decide
     * si informar al usuario o continuar con los datos locales.
     */
    suspend fun syncMovies()
}
