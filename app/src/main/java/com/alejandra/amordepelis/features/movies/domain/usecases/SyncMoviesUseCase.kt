package com.alejandra.amordepelis.features.movies.domain.usecases

import com.alejandra.amordepelis.features.movies.domain.repositories.MoviesRepository
import javax.inject.Inject

/**
 * Caso de uso para sincronizar películas desde el servidor remoto hacia Room.
 *
 * Principio SOLID - Single Responsibility:
 *   Solo orquesta la sincronización; no decide cuándo ni desde dónde llamarla.
 *
 * Principio SOLID - Dependency Inversion:
 *   Depende de la abstracción [MoviesRepository].
 *
 * Al terminar exitosamente, Room notifica automáticamente a [GetMoviesUseCase]
 * (vía Flow), actualizando la UI sin intervención adicional del ViewModel.
 */
class SyncMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {
    /**
     * Ejecuta la sincronización.
     *
     * @return [Result.success] si la sincronización fue exitosa;
     *         [Result.failure] con la excepción si ocurrió un error.
     */
    suspend operator fun invoke(): Result<Unit> = runCatching {
        repository.syncMovies()
    }
}