package com.alejandra.amordepelis.features.home.data.datasources.remote

import com.alejandra.amordepelis.features.home.data.datasources.remote.api.HomeApi
import com.alejandra.amordepelis.features.home.data.datasources.remote.mapper.toHomeMovieList
import com.alejandra.amordepelis.features.home.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.data.datasources.remote.model.MovieDto
import javax.inject.Inject

/**
 * DataSource remoto para operaciones de películas desde la API
 * Implementa el patrón de abstracción para separar concerns
 * Principio SOLID: Single Responsibility - solo maneja operaciones remotas
 */
class RemoteMovieDataSource @Inject constructor(
    private val homeApi: HomeApi
) {

    /**
     * Obtiene todas las películas desde el servidor remoto
     * @return Lista de películas desde la API
     * @throws Exception si hay error en la conexión o procesamiento
     */
    suspend fun getAllMovies(): List<MovieDto> {
        return homeApi.getAllMovies()
    }

    /**
     * Obtiene todas las películas convertidas a entidades de dominio
     * Aplica el mapeo durante la obtención
     * @return Lista de películas mapeadas al dominio
     */
    suspend fun getAllMoviesAsDomain(): List<Movie> {
        return getAllMovies().toHomeMovieList()
    }
}
