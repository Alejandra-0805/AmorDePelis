package com.alejandra.amordepelis.features.movies.domain.usecases

import com.alejandra.amordepelis.features.movies.domain.entities.Movie
import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener el stream reactivo de películas.
 *
 * Principio SOLID - Single Responsibility:
 *   Solo encapsula la lógica de obtener el flujo de películas.
 *
 * Principio SOLID - Dependency Inversion:
 *   Depende de la abstracción [MoviesRepository], no de su implementación.
 *
 * Retorna un [Flow] que:
 *   - Emite inmediatamente los datos cacheados en Room.
 *   - Se actualiza automáticamente cuando [SyncMoviesUseCase] persiste
 *     nuevos datos en la base de datos local.
 */
class GetMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    /**
     * Invoca el caso de uso.
     *
     * @return [Flow] continuo que emite la lista de películas desde Room.
     */
    operator fun invoke(): Flow<List<Movie>> = repository.getMoviesStream()
}
